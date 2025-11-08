package com.example.employees.ui

import com.example.data.models.Department
import com.example.data.models.SortType

sealed interface EmployeesIntent {
    data object Refresh : EmployeesIntent
    data class SearchQueryChanged(val query: String) : EmployeesIntent
    data class DepartmentSelected(val department: Department?) : EmployeesIntent
    data class SortTypeSelected(val sortType: SortType) : EmployeesIntent
    data object ShowSortBottomSheet : EmployeesIntent
    data object HideSortBottomSheet : EmployeesIntent
}