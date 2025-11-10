package com.example.profile.usecases

import com.example.data.EmployeesRepository
import com.example.data.models.Employee
import javax.inject.Inject

class GetEmployeeUseCase @Inject constructor(private val repository: EmployeesRepository) {
    suspend operator fun invoke(
        employeeId: String
    ): Result<Employee> = repository.getEmployee(employeeId)
}