package com.example.employees

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.Department
import com.example.data.models.EmployeesFilter
import com.example.data.models.SortType
import com.example.employees.ui.EmployeesIntent
import com.example.employees.ui.EmployeesScreenState
import com.example.employees.usecases.GetEmployeesUseCase
import com.example.employees.usecases.LoadEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val getEmployees: GetEmployeesUseCase,
    private val loadEmployees: LoadEmployeesUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<EmployeesScreenState> =
        MutableStateFlow(EmployeesScreenState.Initial)
    val state: StateFlow<EmployeesScreenState> = _state.asStateFlow()

    private val filter = MutableStateFlow(EmployeesFilter())

    init {
        refreshEmployees()
    }

    private fun refreshEmployees() {
        viewModelScope.launch {
            when (val screenState = _state.value) {
                EmployeesScreenState.Initial -> {
                    loadEmployees().onSuccess {
                        _state.update { EmployeesScreenState.Employees() }
                        setupFilter()
                    }.onFailure {
                        _state.update { EmployeesScreenState.Error() }
                        Log.d("viewModel", ": ${it.toString()}")
                    }
                }

                is EmployeesScreenState.Employees -> {
                    _state.update { screenState.copy(isLoading = true) }
                    loadEmployees().onSuccess {
                        _state.update { screenState.copy(isLoading = false) }
                    }.onFailure {
                        _state.update { screenState.copy(isLoading = false) }
                        Log.d("viewModel", ": ${it.toString()}")
                    }
                }

                is EmployeesScreenState.Error -> {
                    loadEmployees().onSuccess {
                        _state.update { EmployeesScreenState.Employees() }
                        setupFilter()
                    }.onFailure {
                        _state.update { EmployeesScreenState.Error() }
                        Log.d("viewModel", ": ${it.toString()}")
                    }
                }
            }
        }
    }

    private fun setupFilter() {
        filter
            .onEach { filter ->
                with(filter) {
                    _state.update { previousState ->
                        if (previousState is EmployeesScreenState.Employees) {
                            previousState.copy(
                                searchQuery = searchQuery,
                                selectedDepartment = department,
                                selectedSort = sortType
                            )
                        } else {
                            previousState
                        }
                    }
                }
            }.flatMapLatest { filter ->
                getEmployees(filter)
            }.onEach { employees ->
                _state.update { previousState ->
                    if (previousState is EmployeesScreenState.Employees) {
                        previousState.copy(
                            employees = employees,
                            isLoading = false
                        )
                    } else {
                        previousState
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun handleIntent(intent: EmployeesIntent) {
        when (intent) {
            is EmployeesIntent.SearchQueryChanged -> updateSearchQuery(intent.query)
            is EmployeesIntent.Refresh -> refreshEmployees()
            is EmployeesIntent.DepartmentSelected -> setDepartmentFilter(intent.department)
            is EmployeesIntent.SortTypeSelected -> selectSortType(intent.sortType)
        }
    }

    private fun updateSearchQuery(query: String) {
        filter.update { it.copy(searchQuery = query) }
    }

    private fun setDepartmentFilter(department: Department?) {
        filter.update { it.copy(department = department) }
    }

    private fun selectSortType(sortType: SortType) {
        filter.update { it.copy(sortType = sortType) }
    }
}