package com.example.employees

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.Department
import com.example.data.models.EmployeesFilter
import com.example.employees.usecases.GetEmployeesUseCase
import com.example.employees.usecases.LoadEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val getEmployees: GetEmployeesUseCase,
    private val loadEmployees: LoadEmployeesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            loadEmployees().onSuccess {
                fetchEmployees(query = "LK", department = Department.Android)
            }
                .onFailure {
                    Log.d("viewModel", ": ${it.toString()}")
                }
        }
    }

    fun fetchEmployees(query: String = "", department: Department? = null) {
        viewModelScope.launch {
            getEmployees.invoke(
                employeesFilter = EmployeesFilter(
                    department = department,
                    searchQuery = query
                )
            )
                .collect { Log.d("viewModel", "fetchData: $it") }
        }
    }
}