package io.github.hanihashemi.tomaten.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

/**
 * Android-specific wrapper for SharedMainViewModel
 * Integrates with Android's ViewModel lifecycle and provides Android-specific functionality
 */
class AndroidMainViewModel : ViewModel() {
    private val sharedViewModel = object : SharedMainViewModel() {
        override val viewModelScope: CoroutineScope
            get() = this@AndroidMainViewModel.viewModelScope

        override fun getCurrentTimeMillis(): Long {
            return System.currentTimeMillis()
        }
    }

    // Delegate all properties and methods to the shared ViewModel
    val uiState = sharedViewModel.uiState
    val actions = sharedViewModel.actions

    fun updateState(transform: (io.github.hanihashemi.tomaten.ui.states.SharedUIState) -> io.github.hanihashemi.tomaten.ui.states.SharedUIState) {
        sharedViewModel.updateState(transform)
    }

    override fun onCleared() {
        super.onCleared()
        sharedViewModel.onCleared()
    }
}