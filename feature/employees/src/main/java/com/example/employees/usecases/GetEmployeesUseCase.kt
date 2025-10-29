package com.example.employees.usecases

import com.example.data.EmployeesRepository
import com.example.data.models.Employee
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(private val repository: EmployeesRepository) {
    suspend operator fun invoke(
        searchQuery: String,
        department: String
    ): Flow<List<Employee>> = repository.getEmployees(searchQuery, department)
}