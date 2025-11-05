package com.example.data

import android.util.Log
import com.example.data.mapper.EmployeeMapper
import com.example.data.models.Department
import com.example.data.models.Employee
import com.example.data.models.EmployeesFilter
import com.example.data.models.SortType
import com.example.network.KodeStaffApiService
import com.example.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: KodeStaffApiService,
    private val mapper: EmployeeMapper,
) : EmployeesRepository {

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())


    override suspend fun getEmployees(employeesFilter: EmployeesFilter): Flow<List<Employee>> {
        return _employees.map { employees ->
            employees
                .filterByDepartment(employeesFilter.department)
                .filterBySearchQuery(employeesFilter.searchQuery)
                .sortEmployees(employeesFilter.sortType)
        }
    }

    override suspend fun loadEmployees(): Result<Unit> {
        return runCatching {
            val employeesList =
                withContext(context = Dispatchers.IO) {
                    mapper.responseDtoToListModel(apiService.getEmployees())
                }
            _employees.value = employeesList
            Log.d("repo", "loadEmployees: $employeesList")
        }
    }

    private fun List<Employee>.filterByDepartment(department: Department?): List<Employee> {
        return if (department == null) this
        else this.filter { it.department == department }
    }

    private fun List<Employee>.filterBySearchQuery(query: String): List<Employee> {
        return if (query.isBlank()) this
        else this.filter { user ->
            user.firstName.contains(query, ignoreCase = true) ||
                    user.lastName.contains(query, ignoreCase = true) ||
                    user.userTag.contains(query, ignoreCase = true)
        }
    }

    private fun List<Employee>.sortEmployees(sortType: SortType): List<Employee> {
        return when (sortType) {
            SortType.ALPHABETIC -> sortedBy { it.firstName }
            SortType.BIRTHDAY -> sortByBirthdays()
        }
    }

    private fun List<Employee>.sortByBirthdays(): List<Employee> {
        return sortedBy { employee ->
            DateUtils.calculateDaysUntilNextBirthday(employee.birthday)
        }
    }
}