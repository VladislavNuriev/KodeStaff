package com.example.employees.ui

import com.example.data.models.Department
import com.example.data.models.SortType
import com.example.employees.models.EmployeeUi

sealed interface EmployeesScreenState {
    data object Initial : EmployeesScreenState
    data class Employees(
        val isLoading: Boolean = false,
        val employees: List<EmployeeUi> = emptyList(),
        val searchQuery: String = "",
        val selectedDepartment: Department? = null,
        val selectedSort: SortType = SortType.ALPHABETIC,
        val isSortBottomSheetVisible: Boolean = false
    ) : EmployeesScreenState

    data class Error(
        val error: String? = null,
    ) : EmployeesScreenState
}