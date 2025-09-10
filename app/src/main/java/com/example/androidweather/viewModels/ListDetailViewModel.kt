package com.example.androidweather.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidweather.models.Weather
import com.example.androidweather.models.WeatherDetail
import com.example.androidweather.models.sampleItemDetails
import com.example.androidweather.models.sampleItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ListDetailUiState(
    val items: List<Weather> = emptyList(),
    val selectedItemId: String? = null,
    val selectedItemDetails: WeatherDetail? = null,
    val isLoadingDetails: Boolean = false,
    val showDetailPaneFullScreen: Boolean = false // For compact screen navigation
)

class ListDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ListDetailUiState())
    val uiState: StateFlow<ListDetailUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        // In a real app, fetch from a repository
        _uiState.update { it.copy(items = sampleItems) }
    }

    fun selectItem(itemId: String) {
        _uiState.update {
            it.copy(
                selectedItemId = itemId,
                isLoadingDetails = true,
                showDetailPaneFullScreen = true // Trigger navigation on compact
            )
        }
        // Simulate fetching details
        viewModelScope.launch {
            // In a real app, fetch from a repository asynchronously
            kotlinx.coroutines.delay(300) // Simulate network delay
            val details = sampleItemDetails[itemId]
            _uiState.update {
                it.copy(
                    selectedItemDetails = details,
                    isLoadingDetails = false
                )
            }
        }
    }

    fun clearSelection() { // Or navigateBackFromDetail
        _uiState.update {
            it.copy(
                // selectedItemId = null, // Keep selectedId if you want to highlight it in the list
                // selectedItemDetails = null, // Keep details loaded if you want to show them immediately next time
                showDetailPaneFullScreen = false // Navigate back to list on compact
            )
        }
    }
}