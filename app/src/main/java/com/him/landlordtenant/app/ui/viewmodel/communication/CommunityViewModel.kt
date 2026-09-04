package com.him.landlordtenant.app.ui.viewmodel.communication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.model.communication.ChatMessage
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.ChatRepository
import com.him.landlordtenant.app.interfaces.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    private val _announcements = MutableStateFlow<List<ChatMessage>>(emptyList())
    val announcements: StateFlow<List<ChatMessage>> = _announcements.asStateFlow()

    private val _propertyName = MutableStateFlow("Building Community")
    val propertyName: StateFlow<String> = _propertyName.asStateFlow()

    private val _isLandlord = MutableStateFlow(false)
    val isLandlord: StateFlow<Boolean> = _isLandlord.asStateFlow()

    fun loadCommunityData(apartmentId: String) {
        viewModelScope.launch {
            val userResult = authRepository.getCurrentUser()
            val user = userResult.getOrNull()
            _isLandlord.value = user?.roles?.contains("LANDLORD") ?: false

            propertyRepository.getProperty(apartmentId).onSuccess {
                _propertyName.value = it.name
            }

            chatRepository.getAnnouncements(apartmentId)
                .onEach { _announcements.value = it }
                .catch { e -> Log.e("CommunityViewModel", "Error loading announcements", e) }
                .launchIn(viewModelScope)
        }
    }

    fun postAnnouncement(apartmentId: String, text: String) {
        viewModelScope.launch {
            val landlordId = authRepository.getCurrentUserId() ?: return@launch
            chatRepository.postAnnouncement(landlordId, apartmentId, text)
        }
    }
}
