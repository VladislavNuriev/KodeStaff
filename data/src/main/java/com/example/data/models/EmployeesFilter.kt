package com.example.data.models

data class EmployeesFilter(
    val department: Department? = null,
    val searchQuery: String = "",
    val sortType: SortType = SortType.ALPHABETIC
)