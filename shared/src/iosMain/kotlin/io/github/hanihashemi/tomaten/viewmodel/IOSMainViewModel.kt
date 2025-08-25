package io.github.hanihashemi.tomaten.viewmodel

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

/**
 * iOS-specific wrapper for SharedMainViewModel
 * Provides iOS-specific functionality and integrates with iOS patterns
 */
class IOSMainViewModel : SharedMainViewModel() {
    
    override fun getCurrentTimeMillis(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }

    // iOS-specific methods can be added here
    fun startTimerWithHapticFeedback() {
        // Could trigger iOS haptic feedback before starting timer
        startOrStopTimer()
    }

    // For iOS SwiftUI integration
    fun getTimerState() = uiState.value.timer
    fun getTagState() = uiState.value.tag
    fun getLoginState() = uiState.value.login
}