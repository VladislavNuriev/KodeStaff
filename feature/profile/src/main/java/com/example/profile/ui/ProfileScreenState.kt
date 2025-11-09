package com.example.profile.ui

import com.example.profile.models.EmployeeUi

sealed interface ProfileScreenState {
    data object Initial : ProfileScreenState
    data class Profile(
        val employee: EmployeeUi,
    ) : ProfileScreenState

    data class Error(
        val error: String? = null,
    ) : ProfileScreenState

    data object Finished : ProfileScreenState
}