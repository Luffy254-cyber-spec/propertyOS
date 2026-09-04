package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.DocumentData
import com.him.landlordtenant.app.interfaces.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _documents = MutableStateFlow<List<DocumentData>>(emptyList())
    val documents: StateFlow<List<DocumentData>> = _documents.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadDocuments()
    }

    fun loadDocuments() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            documentRepository.getUserDocuments(userId).onSuccess {
                _documents.value = it.sortedByDescending { d -> d.createdAt }
            }
            _isLoading.value = false
        }
    }
}
