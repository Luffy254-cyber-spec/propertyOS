package com.him.landlordtenant.app.interfaces

import com.him.landlordtenant.app.data.model.billing.*
import kotlinx.coroutines.flow.Flow

interface PropertyBillingRepository {
    // Fee Configuration
    suspend fun getFeeConfigurations(tenantId: String, unitId: String): Result<List<FeeConfiguration>>
    suspend fun saveFeeConfiguration(config: FeeConfiguration): Result<Unit>
    
    // Invoicing
    suspend fun createInvoice(invoice: Invoice): Result<String>
    suspend fun getInvoice(invoiceId: String): Result<Invoice>
    suspend fun addInvoiceItem(invoiceId: String, item: InvoiceItem): Result<Unit>
    suspend fun getInvoiceItems(invoiceId: String): Result<List<InvoiceItem>>
    
    // Metering
    suspend fun saveMeterReading(reading: MeterReading): Result<Unit>
    suspend fun getLatestMeterReading(meterId: String): Result<MeterReading?>
    
    // Arrears & Balance
    suspend fun getTotalOutstanding(tenantId: String): Result<Double>
    suspend fun getActiveInvoices(tenantId: String): Result<List<Invoice>>
    
    // Payments
    suspend fun recordPayment(payment: Payment): Result<String>
    suspend fun allocatePayment(allocation: PaymentAllocation): Result<Unit>
}
