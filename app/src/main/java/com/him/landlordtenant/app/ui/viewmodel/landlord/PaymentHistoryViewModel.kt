package com.him.landlordtenant.app.ui.viewmodel.landlord

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.PaymentRepository
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentHistoryUIModel
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentTransactionStatus
import com.google.firebase.functions.FirebaseFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PaymentHistoryViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val authRepository: AuthRepository,
    private val firebaseFunctions: FirebaseFunctions
) : ViewModel() {

    private val _payments = MutableStateFlow<List<TenantPaymentHistoryUIModel>>(emptyList())
    val payments: StateFlow<List<TenantPaymentHistoryUIModel>> = _payments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadPayments()
    }

    fun loadPayments() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            
            paymentRepository.getLandlordPayments(userId).onSuccess { list ->
                _payments.value = list.map { p ->
                    TenantPaymentHistoryUIModel(
                        id = p.id,
                        amount = p.amount,
                        date = p.createdAt,
                        reference = p.providerReference ?: p.transactionReference ?: "N/A",
                        status = try { TenantPaymentTransactionStatus.valueOf(p.status.name) } catch(e: Exception) { TenantPaymentTransactionStatus.SUCCESSFUL },
                        description = "Payment via ${p.method}",
                        method = p.method
                    )
                }
            }
            _isLoading.value = false
        }
    }

    fun exportToExcel(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = firebaseFunctions
                    .getHttpsCallable("exportPaymentsToExcel")
                    .call()
                    .await()

                val data = result.data as? Map<String, Any> ?: throw Exception("Invalid response")
                val url = data["url"] as String

                // Launch browser to download or share the URL
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)

            } catch (e: Exception) {
                Log.e("PaymentHistoryVM", "Export failed", e)
            }
            _isLoading.value = false
        }
    }
}
