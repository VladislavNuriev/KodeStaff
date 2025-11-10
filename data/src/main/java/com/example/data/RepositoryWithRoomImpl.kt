package com.example.data

import com.example.data.mapper.EmployeeMapper
import com.example.data.models.Employee
import com.example.data.models.EmployeesFilter
import com.example.database.EmployeeDao
import com.example.network.KodeStaffApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


//class RepositoryWithRoomImpl @Inject constructor(
//    private val apiService: KodeStaffApiService,
//    private val dao: EmployeeDao,
//    private val mapper: EmployeeMapper,
//) : EmployeesRepository {
//
//    override suspend fun getEmployees(
//        employeesFilter: EmployeesFilter
//    ): Flow<List<Employee>> {
//        return dao.getEmployees(
//            employeesFilter.searchQuery,
//            employeesFilter.department?.stringName ?: ""
//        ).map {
//            it.map { employeeEntity ->
//                mapper.entityToModel(employeeEntity)
//            }
//        }
//    }
//
//    override suspend fun loadEmployees(): Result<Unit> {
//        return runCatching {
//            val employeesList =
//                withContext(context = Dispatchers.IO) {
//                    mapper.responseDtoToListEntity(apiService.getEmployees())
//                }
//            dao.insertEmployees(employeesList)
//        }
//    }
//}