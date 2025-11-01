package com.example.employees.usecases

import com.example.data.EmployeesRepository
import javax.inject.Inject

class LoadEmployeesUseCase @Inject constructor(private val employeesRepository: EmployeesRepository) {
    suspend operator fun invoke(): Result<Unit> = employeesRepository.loadEmployees()
}
