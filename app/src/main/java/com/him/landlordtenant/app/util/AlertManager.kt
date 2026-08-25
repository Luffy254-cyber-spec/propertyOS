package com.him.landlordtenant.app.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertManager @Inject constructor() : ViewModel() {

    private val _snackbars = MutableSharedFlow<SnackbarMessage>(extraBufferCapacity = 1)
    val snackbars = _snackbars.asSharedFlow()

    private val _alerts = MutableSharedFlow<AlertMessage>(extraBufferCapacity = 1)
    val alerts = _alerts.asSharedFlow()

    private val _toasts = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val toasts = _toasts.asSharedFlow()

    private val _banners = MutableSharedFlow<BannerMessage?>(extraBufferCapacity = 1)
    val banners = _banners.asSharedFlow()

    suspend fun showSnackbar(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        _snackbars.emit(SnackbarMessage(message, actionLabel, onAction))
    }

    suspend fun showAlert(title: String, message: String, positiveButton: String = "OK", onConfirm: (() -> Unit)? = null) {
        _alerts.emit(AlertMessage(title, message, positiveButton, onConfirm))
    }

    suspend fun showToast(message: String) {
        _toasts.emit(message)
    }

    suspend fun showBanner(message: String, type: BannerType = BannerType.INFO) {
        _banners.emit(BannerMessage(message, type))
    }

    suspend fun hideBanner() {
        _banners.emit(null)
    }
}

data class SnackbarMessage(
    val message: String,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)

data class AlertMessage(
    val title: String,
    val message: String,
    val positiveButton: String = "OK",
    val onConfirm: (() -> Unit)? = null
)

data class BannerMessage(
    val message: String,
    val type: BannerType
)

enum class BannerType {
    INFO, SUCCESS, WARNING, ERROR
}
