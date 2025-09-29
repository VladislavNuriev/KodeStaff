package com.example.data

import com.example.data.models.Employee
import kotlinx.coroutines.flow.Flow

interface EmployeesRepository {
    suspend fun getEmployees(
        searchQuery: String,
        department: String
    ): Flow<List<Employee>>
}