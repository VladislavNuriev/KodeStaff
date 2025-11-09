package com.example.profile

import androidx.lifecycle.ViewModel
import com.example.profile.models.EmployeeUi
import com.example.profile.ui.ProfileIntent
import com.example.profile.ui.ProfileScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
//class ProfileViewModel @AssistedInject constructor(
//    @Assisted("employee") private val employee: EmployeeUi
//) : ViewModel() {

class ProfileViewModel(
    private val employee: EmployeeUi
) : ViewModel() {

    private val _state: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState.Initial)
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    init {

    }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.Back -> _state.update { ProfileScreenState.Finished }
        }
    }
}