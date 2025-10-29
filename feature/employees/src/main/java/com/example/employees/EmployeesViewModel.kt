package com.example.employees

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employees.usecases.GetEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val getEmployees: GetEmployeesUseCase
) : ViewModel() {
    fun fetchData(query: String = "", department: String = "") {
        viewModelScope.launch {
            getEmployees.invoke(searchQuery = query, department = department)
                .collect { Log.d("viewModel", "fetchData: $it") }
        }
    }
}