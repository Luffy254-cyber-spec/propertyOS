package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.data.model.billing.*
import com.him.landlordtenant.app.interfaces.PropertyBillingRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BillingRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : PropertyBillingRepository {

    override suspend fun getFeeConfigurations(tenantId: String, unitId: String): Result<List<FeeConfiguration>> = try {
        val snapshot = firestoreDataSource.collection("fee_configurations")
            .whereEqualTo("tenantId", tenantId)
            .whereEqualTo("unitId", unitId)
            .get()
            .await()
        Result.success(snapshot.toObjects(FeeConfiguration::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun saveFeeConfiguration(config: FeeConfiguration): Result<Unit> = try {
        val id = if (config.id.isEmpty()) firestoreDataSource.collection("fee_configurations").document().id else config.id
        firestoreDataSource.saveData("fee_configurations", id, config.copy(id = id))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun createInvoice(invoice: Invoice): Result<String> = try {
        val docRef = firestoreDataSource.collection("invoices").document()
        val id = docRef.id
        firestoreDataSource.saveData("invoices", id, invoice.copy(id = id)).map { id }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getInvoice(invoiceId: String): Result<Invoice> = try {
        val data = firestoreDataSource.getData("invoices", invoiceId, Invoice::class.java).getOrThrow()
        Result.success(data ?: throw Exception("Invoice not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addInvoiceItem(invoiceId: String, item: InvoiceItem): Result<Unit> = try {
        val id = firestoreDataSource.collection("invoices").document(invoiceId).collection("items").document().id
        firestoreDataSource.saveData("invoices/$invoiceId/items", id, item.copy(id = id, invoiceId = invoiceId))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getInvoiceItems(invoiceId: String): Result<List<InvoiceItem>> = try {
        val snapshot = firestoreDataSource.collection("invoices").document(invoiceId).collection("items").get().await()
        Result.success(snapshot.toObjects(InvoiceItem::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun saveMeterReading(reading: MeterReading): Result<Unit> = try {
        val id = if (reading.id.isEmpty()) firestoreDataSource.collection("meter_readings").document().id else reading.id
        firestoreDataSource.saveData("meter_readings", id, reading.copy(id = id))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getLatestMeterReading(meterId: String): Result<MeterReading?> = try {
        val snapshot = firestoreDataSource.collection("meter_readings")
            .whereEqualTo("meterId", meterId)
            .orderBy("readingDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
        Result.success(snapshot.toObjects(MeterReading::class.java).firstOrNull())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getTotalOutstanding(tenantId: String): Result<Double> = try {
        val snapshot = firestoreDataSource.collection("invoices")
            .whereEqualTo("tenantId", tenantId)
            .whereIn("status", listOf(BillingStatus.GENERATED.name, BillingStatus.PENDING_PAYMENT.name, BillingStatus.PARTIALLY_PAID.name, BillingStatus.OVERDUE.name))
            .get()
            .await()
        val invoices = snapshot.toObjects(Invoice::class.java)
        Result.success(invoices.sumOf { it.outstandingAmount })
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun recordPayment(payment: Payment): Result<String> = try {
        val id = firestoreDataSource.collection("payments").document().id
        firestoreDataSource.saveData("payments", id, payment.copy(id = id)).map { id }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun allocatePayment(allocation: PaymentAllocation): Result<Unit> = try {
        val id = firestoreDataSource.collection("payment_allocations").document().id
        firestoreDataSource.saveData("payment_allocations", id, allocation.copy(id = id))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
