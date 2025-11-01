package com.example.data

import com.example.data.models.Employee
import com.example.data.models.EmployeesFilter
import kotlinx.coroutines.flow.Flow

interface EmployeesRepository {
    suspend fun getEmployees(
        employeesFilter: EmployeesFilter
    ): Flow<List<Employee>>

    suspend fun loadEmployees(): Result<Unit>
}