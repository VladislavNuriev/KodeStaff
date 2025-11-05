package com.example.employees.ui

import com.example.data.models.Department
import com.example.data.models.Employee
import com.example.data.models.SortType

sealed interface EmployeesScreenState {
    data object Initial : EmployeesScreenState
    data class Employees(
        val isLoading: Boolean = false,
        val employees: List<Employee> = emptyList(),
        val searchQuery: String = "",
        val selectedDepartment: Department? = null,
        val selectedSort: SortType = SortType.ALPHABETIC
    ) : EmployeesScreenState

    data class Error(
        val error: String? = null,
    ) : EmployeesScreenState
}