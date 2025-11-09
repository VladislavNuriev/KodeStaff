package com.example.profile.ui

sealed interface ProfileIntent {
    data object Back : ProfileIntent
}