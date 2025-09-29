package com.example.data

import com.example.data.mapper.EmployeeMapper
import com.example.data.models.Employee
import com.example.database.EmployeeDao
import com.example.network.KodeStaffApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val apiService: KodeStaffApiService,
    private val dao: EmployeeDao,
    private val mapper: EmployeeMapper,
) : EmployeesRepository {

    override suspend fun getEmployees(
        searchQuery: String,
        department: String,
    ): Flow<List<Employee>> {
        loadData()
        delay(5000)
        return dao.getEmployees(searchQuery, department).map {
            it.map { employeeEntity ->
                mapper.entityToModel(employeeEntity)
            }
        }
    }

    suspend fun loadData(): Result<Unit> {
        return runCatching {
            val employeesList =
                withContext(context = Dispatchers.IO) {
                    mapper.responseDtoToListEntity(apiService.getEmployees())
                }
            dao.insertEmployees(employeesList)
        }
    }
}