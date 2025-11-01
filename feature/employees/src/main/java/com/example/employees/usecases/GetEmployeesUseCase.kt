package com.example.employees.usecases

import com.example.data.EmployeesRepository
import com.example.data.models.Employee
import com.example.data.models.EmployeesFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(private val repository: EmployeesRepository) {
    suspend operator fun invoke(
        employeesFilter: EmployeesFilter
    ): Flow<List<Employee>> = repository.getEmployees(employeesFilter)
}