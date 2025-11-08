package com.example.employees.models

import com.example.data.models.Department

data class EmployeeUi(
    val id: String,
    val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val userTag: String,
    val department: Department,
    val position: String,
    val birthday: String,
    val phone: String
)