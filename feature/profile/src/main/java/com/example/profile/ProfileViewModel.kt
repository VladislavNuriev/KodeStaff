package com.example.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profile.models.MapperPresentation
import com.example.profile.ui.ProfileIntent
import com.example.profile.ui.ProfileScreenState
import com.example.profile.usecases.GetEmployeeUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
class ProfileViewModel @AssistedInject constructor(
    @Assisted("employeeId") private val employeeId: String,
    val getEmployee: GetEmployeeUseCase,
    val mapper: MapperPresentation
) : ViewModel() {

    private val _state: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState.Initial)
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getEmployee.invoke(employeeId)
                .onSuccess { employee ->
                    _state.update {
                        ProfileScreenState.Profile(
                            mapper.employeeModelToEmployeeUi(
                                employee
                            )
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { ProfileScreenState.Error(error.message) }
                }
        }
    }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.Back -> _state.update { ProfileScreenState.Finished }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("employeeId") employeeId: String
        ): ProfileViewModel
    }
}