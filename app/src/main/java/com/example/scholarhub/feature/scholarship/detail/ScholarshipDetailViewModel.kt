package com.example.scholarhub.feature.scholarship.detail

import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scholarhub.domain.scholarship.Scholarship
import com.example.scholarhub.domain.scholarship.ScholarshipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ScholarshipDetailViewModel(
    private val scholarshipId: String,
    private val repository: ScholarshipRepository,
) : ViewModel() {
    private val state = MutableStateFlow<ScholarshipDetailState>(ScholarshipDetailState.Loading)
    val uiState = state.asStateFlow()

    init {
        load()
    }
    fun load() {
        state.value = ScholarshipDetailState.Loading
        viewModelScope.launch {
            state.value = repository.getById(scholarshipId).fold(
                onSuccess = { scholarship ->
                    scholarship?.let(ScholarshipDetailState::Ready) ?: ScholarshipDetailState.NotFound
                },
                onFailure = { ScholarshipDetailState.Error },
            )
        }
    }
}

sealed interface ScholarshipDetailState {
    data object Loading : ScholarshipDetailState
    data object NotFound : ScholarshipDetailState
    data object Error : ScholarshipDetailState
    data class Ready(val scholarship: Scholarship) : ScholarshipDetailState
}
