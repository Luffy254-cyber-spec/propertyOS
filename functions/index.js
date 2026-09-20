const functions = require("firebase-functions");
const admin = require("firebase-admin");
const axios = require("axios");
const cloudinary = require("cloudinary").v2;
const ExcelJS = require("exceljs");

// Load secrets from environment variables (automatically populated from .env in Firebase Functions v3.18+)
const STRIPE_SECRET_KEY = process.env.STRIPE_SECRET_KEY;
const CLOUDINARY_CLOUD_NAME = process.env.CLOUDINARY_CLOUD_NAME;
const CLOUDINARY_API_KEY = process.env.CLOUDINARY_API_KEY;
const CLOUDINARY_API_SECRET = process.env.CLOUDINARY_API_SECRET;
const MASTER_MPESA_CONSUMER_KEY = process.env.MASTER_MPESA_CONSUMER_KEY;
const MASTER_MPESA_CONSUMER_SECRET = process.env.MASTER_MPESA_CONSUMER_SECRET;
const MASTER_MPESA_PASSKEY = process.env.MASTER_MPESA_PASSKEY;

const stripe = require("stripe")(STRIPE_SECRET_KEY);

admin.initializeApp();

// Cloudinary Configuration
cloudinary.config({
  cloud_name: CLOUDINARY_CLOUD_NAME,
  api_key: CLOUDINARY_API_KEY,
  api_secret: CLOUDINARY_API_SECRET,
  secure: true
});

/**
 * Helper: Handle Successful Payment
 * Automates Tallying, Record Tracking, and Receipt Generation
 */
async function handleSuccessfulPayment(paymentId, amount, providerRef, method) {
  const db = admin.firestore();
  const paymentRef = db.collection("payments").doc(paymentId);
  const paymentSnap = await paymentRef.get();

  if (!paymentSnap.exists) return;
  const payment = paymentSnap.data();
  if (payment.status === "SUCCESS") return; // Already processed

  const batch = db.batch();

  // 1. Update Payment Record
  batch.update(paymentRef, {
    status: "SUCCESS",
    completedAt: admin.firestore.FieldValue.serverTimestamp(),
    providerReference: providerRef
  });

  // 2. Auto-Tally Invoice (If applicable)
  if (payment.billId) {
    const invoiceRef = db.collection("invoices").doc(payment.billId);
    const invoiceSnap = await invoiceRef.get();
    if (invoiceSnap.exists) {
      const invoice = invoiceSnap.data();
      const newPaid = (invoice.amountPaid || 0) + amount;
      const newOutstanding = invoice.totalDue - newPaid;

      batch.update(invoiceRef, {
        amountPaid: newPaid,
        outstandingAmount: newOutstanding,
        status: newOutstanding <= 0 ? "PAID" : "PARTIALLY_PAID",
        updatedAt: admin.firestore.FieldValue.serverTimestamp()
      });
    }
  }

  // 3. Generate Digital Receipt
  const receiptId = `RCP_${Date.now()}`;
  const receiptRef = db.collection("receipts").doc(receiptId);
  batch.set(receiptRef, {
    id: receiptId,
    receiptNumber: `R-${Math.random().toString(36).substr(2, 9).toUpperCase()}`,
    paymentId: paymentId,
    tenantId: payment.tenantId,
    invoiceId: payment.billId || null,
    amount: amount,
    paymentMethod: method,
    providerTransactionId: providerRef,
    paymentDate: admin.firestore.FieldValue.serverTimestamp(),
    createdAt: admin.firestore.FieldValue.serverTimestamp()
  });

  // 4. Update Property/Landlord Revenue Tally
  if (payment.propertyId && payment.landlordId) {
     const landlordRef = db.collection("landlords").doc(payment.landlordId);
     batch.update(landlordRef, {
       totalRevenue: admin.firestore.FieldValue.increment(amount),
       lastPaymentReceived: admin.firestore.FieldValue.serverTimestamp()
     });
  }

  await batch.commit();
}

/**
 * 1. Secure M-Pesa STK Push
 */
exports.initiateMpesaStk = functions.https.onCall(async (data, context) => {
  if (context.app === undefined) throw new functions.https.HttpsError("failed-precondition", "Anti-bot check failed.");
  if (!context.auth) throw new functions.https.HttpsError("unauthenticated", "User must be logged in.");

  const { paymentId, phoneNumber } = data;
  const paymentSnap = await admin.firestore().collection("payments").doc(paymentId).get();
  if (!paymentSnap.exists) throw new functions.https.HttpsError("not-found", "Payment record not found.");

  const payment = paymentSnap.data();
  const propertyId = payment.propertyId;
  const amount = Math.round(payment.amount);

  const configSnap = await admin.firestore().collection("payment_configs").doc(propertyId).get();
  if (!configSnap.exists) throw new functions.https.HttpsError("failed-precondition", "M-Pesa config missing.");

  const config = configSnap.data();

  try {
    const auth = Buffer.from(`${MASTER_MPESA_CONSUMER_KEY}:${MASTER_MPESA_CONSUMER_SECRET}`).toString("base64");
    const tokenRes = await axios.get("https://api.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials", {
      headers: { Authorization: `Basic ${auth}` }
    });
    const accessToken = tokenRes.data.access_token;

    const timestamp = new Date().toISOString().replace(/[^0-9]/g, "").slice(0, 14);
    const password = Buffer.from(`${config.mpesaShortCode}${MASTER_MPESA_PASSKEY}${timestamp}`).toString("base64");

    const stkRes = await axios.post("https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest", {
      BusinessShortCode: config.mpesaShortCode,
      Password: password,
      Timestamp: timestamp,
      TransactionType: "CustomerPayBillOnline",
      Amount: amount,
      PartyA: phoneNumber.replace("+", ""),
      PartyB: config.mpesaShortCode,
      PhoneNumber: phoneNumber.replace("+", ""),
      CallBackURL: `https://us-central1-${process.env.GCLOUD_PROJECT}.cloudfunctions.net/mpesaCallback`,
      AccountReference: payment.externalReference || "Rent",
      TransactionDesc: `Rent: ${payment.externalReference || "Unit"}`
    }, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });

    if (stkRes.data.ResponseCode === "0") {
      await admin.firestore().collection("payments").doc(paymentId).update({
        providerReference: stkRes.data.CheckoutRequestID,
        status: "INITIATED"
      });
    }

    return stkRes.data;
  } catch (error) {
    console.error("M-Pesa Gateway Error:", error);
    throw new functions.https.HttpsError("internal", "Gateway error.");
  }
});

/**
 * 2. M-Pesa Webhook (Callback)
 * Receives real-time payment confirmation from Safaricom
 */
exports.mpesaCallback = functions.https.onRequest(async (req, res) => {
  const callbackData = req.body.Body.stkCallback;
  const checkoutRequestId = callbackData.CheckoutRequestID;
  const resultCode = callbackData.ResultCode;

  if (resultCode === 0) {
    const amount = callbackData.CallbackMetadata.Item.find(i => i.Name === "Amount").Value;
    const mpesaReceipt = callbackData.CallbackMetadata.Item.find(i => i.Name === "MpesaReceiptNumber").Value;

    const paymentQuery = await admin.firestore().collection("payments")
      .where("providerReference", "==", checkoutRequestId)
      .limit(1).get();

    if (!paymentQuery.empty) {
      await handleSuccessfulPayment(paymentQuery.docs[0].id, amount, mpesaReceipt, "MPESA");
    }
  }
  res.status(200).send("OK");
});

/**
 * 3. Stripe Webhook
 */
exports.stripeWebhook = functions.https.onRequest(async (req, res) => {
  const sig = req.headers["stripe-signature"];
  let event;

  try {
    // In production, use your webhook secret: stripe.webhooks.constructEvent(req.rawBody, sig, endpointSecret);
    event = req.body;
  } catch (err) {
    return res.status(400).send(`Webhook Error: ${err.message}`);
  }

  if (event.type === "payment_intent.succeeded") {
    const intent = event.data.object;
    const paymentId = intent.metadata.paymentId;
    if (paymentId) {
      await handleSuccessfulPayment(paymentId, intent.amount / 100, intent.id, "CARD");
    }
  }
  res.json({received: true});
});

/**
 * 4. Export Payments to Excel
 */
exports.exportPaymentsToExcel = functions.https.onCall(async (data, context) => {
  if (!context.auth) throw new functions.https.HttpsError("unauthenticated", "User must be logged in.");

  const paymentsSnap = await admin.firestore().collection("payments")
    .where("status", "==", "SUCCESS")
    .orderBy("completedAt", "desc")
    .limit(1000)
    .get();

  const workbook = new ExcelJS.Workbook();
  const worksheet = workbook.addWorksheet("Payments");

  worksheet.columns = [
    { header: "Date", key: "date", width: 20 },
    { header: "Payment ID", key: "id", width: 20 },
    { header: "Method", key: "method", width: 10 },
    { header: "Amount (KES)", key: "amount", width: 15 },
    { header: "Reference", key: "ref", width: 20 },
    { header: "Tenant ID", key: "tenant", width: 30 }
  ];

  paymentsSnap.forEach(doc => {
    const p = doc.data();
    worksheet.addRow({
      date: p.completedAt ? p.completedAt.toDate().toLocaleString() : "N/A",
      id: doc.id,
      method: p.method,
      amount: p.amount,
      ref: p.providerReference,
      tenant: p.tenantId
    });
  });

  const buffer = await workbook.xlsx.writeBuffer();

  // SECURITY: Upload the report to Cloudinary (Raw storage) and return a signed/secure URL
  const uploadRes = await new Promise((resolve, reject) => {
    cloudinary.uploader.upload_stream({
      resource_type: "raw",
      folder: "reports",
      public_id: `payments_report_${Date.now()}.xlsx`
    }, (error, result) => {
      if (error) reject(error);
      else resolve(result);
    }).end(buffer);
  });

  return { url: uploadRes.secure_url };
});

/**
 * 5. Secure Stripe Intent Creation
 */
exports.createStripeIntent = functions.https.onCall(async (data, context) => {
  if (context.app === undefined) throw new functions.https.HttpsError("failed-precondition", "Anti-bot check failed.");
  if (!context.auth) throw new functions.https.HttpsError("unauthenticated", "User must be logged in.");

  const { paymentId } = data;
  const paymentSnap = await admin.firestore().collection("payments").doc(paymentId).get();
  const payment = paymentSnap.data();

  try {
    const paymentIntent = await stripe.paymentIntents.create({
      amount: Math.round(payment.amount * 100),
      currency: "kes",
      metadata: { paymentId: paymentId }
    });
    return { clientSecret: paymentIntent.client_secret };
  } catch (error) {
    throw new functions.https.HttpsError("internal", error.message);
  }
});

/**
 * 6. Cloudinary Signature
 */
exports.generateCloudinarySignature = functions.https.onCall(async (data, context) => {
  if (context.app === undefined) throw new functions.https.HttpsError("failed-precondition", "Anti-bot check failed.");
  if (!context.auth) throw new functions.https.HttpsError("unauthenticated", "User must be logged in.");
  const { paramsToSign } = data;
  try {
    const signature = cloudinary.utils.api_sign_request(paramsToSign, CLOUDINARY_API_SECRET);
    return { signature: signature, apiKey: CLOUDINARY_API_KEY, cloudName: CLOUDINARY_CLOUD_NAME };
  } catch (error) {
    throw new functions.https.HttpsError("internal", "Signature failed.");
  }
});

/**
 * 7. Secure Cloudinary Media Deletion
 */
exports.deleteCloudinaryMedia = functions.https.onCall(async (data, context) => {
  if (context.app === undefined) throw new functions.https.HttpsError("failed-precondition", "Anti-bot check failed.");
  if (!context.auth) throw new functions.https.HttpsError("unauthenticated", "User must be logged in.");

  const { publicId, resourceType } = data; // resourceType: 'image' or 'video'

  try {
    const result = await cloudinary.uploader.destroy(publicId, {
      resource_type: resourceType || "image"
    });
    return result;
  } catch (error) {
    throw new functions.https.HttpsError("internal", "Deletion failed.");
  }
});
