package com.him.landlordtenant.app.util

import android.content.Context
import android.os.Environment
import com.him.landlordtenant.app.data.model.billing.Invoice
import com.him.landlordtenant.app.data.model.billing.Payment
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataExporter @Inject constructor() {

    /**
     * Export billing data to CSV.
     */
    fun exportInvoicesToCsv(context: Context, invoices: List<Invoice>): File? {
        val fileName = "billing_report_${System.currentTimeMillis()}.csv"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        
        return try {
            FileOutputStream(file).use { out ->
                val header = "Invoice Number,Tenant ID,Month,Year,Subtotal,Arrears,Total Due,Amount Paid,Status\n"
                out.write(header.toByteArray())
                
                invoices.forEach { inv ->
                    val row = "${inv.invoiceNumber},${inv.tenantId},${inv.month},${inv.year},${inv.subtotal},${inv.previousArrears},${inv.totalDue},${inv.amountPaid},${inv.status}\n"
                    out.write(row.toByteArray())
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Export payments to CSV.
     */
    fun exportPaymentsToCsv(context: Context, payments: List<Payment>): File? {
        val fileName = "payments_report_${System.currentTimeMillis()}.csv"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        
        return try {
            FileOutputStream(file).use { out ->
                val header = "Reference,Amount,Method,Status,Date\n"
                out.write(header.toByteArray())
                
                payments.forEach { p ->
                    val row = "${p.paymentReference},${p.amount},${p.paymentMethod},${p.status},${java.util.Date(p.createdAt)}\n"
                    out.write(row.toByteArray())
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
