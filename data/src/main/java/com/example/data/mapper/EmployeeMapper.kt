package com.example.data.mapper

import com.example.data.models.Department
import com.example.data.models.Employee
import com.example.database.EmployeeEntity
import com.example.network.dto.EmployeeDto
import com.example.network.dto.ResponseDto
import com.example.util.DateUtils
import javax.inject.Inject

class EmployeeMapper @Inject constructor() {

    private fun dtoToEntity(dto: EmployeeDto): EmployeeEntity {
        with(dto) {
            return EmployeeEntity(
                id = id,
                avatarUrl = avatarUrl,
                firstName = firstName,
                lastName = lastName,
                userTag = userTag,
                department = department,
                position = position,
                birthday = DateUtils.dateToTimestamp(birthday),
                phone = phone
            )
        }
    }


    fun responseDtoToListEntity(responseDto: ResponseDto): List<EmployeeEntity> {
        return responseDto.employees.map {
            dtoToEntity(it)
        }
    }

    fun responseDtoToListModel(responseDto: ResponseDto): List<Employee> {
        return responseDto.employees.map {
            dtoToModel(it)
        }
    }

    fun entityToModel(entity: EmployeeEntity): Employee =
        with(entity) {
            Employee(
                id = id,
                avatarUrl = avatarUrl,
                firstName = firstName,
                lastName = lastName,
                userTag = userTag,
                department = Department.fromString(department),
                position = position,
                birthday = birthday,
                phone = phone
            )
        }

    private fun dtoToModel(dto: EmployeeDto): Employee {
        with(dto) {
            return Employee(
                id = id,
                avatarUrl = avatarUrl,
                firstName = firstName,
                lastName = lastName,
                userTag = userTag,
                department = departmentDtoToModel(department),
                position = position,
                birthday = DateUtils.dateToTimestamp(birthday),
                phone = phone
            )
        }
    }

    private fun departmentDtoToModel(apiName: String): Department {
        return when (apiName) {
            "android" -> Department.fromString("Android")
            "ios" -> Department.fromString("iOS")
            "design" -> Department.fromString("Дизайн")
            "management" -> Department.fromString("Менеджмент")
            "qa" -> Department.fromString("QA")
            "back_office" -> Department.fromString("Бэк-офис")
            "frontend" -> Department.fromString("Frontend")
            "hr" -> Department.fromString("HR")
            "pr" -> Department.fromString("PR")
            "backend" -> Department.fromString("Backend")
            "support" -> Department.fromString("Техподдержка")
            "analytics" -> Department.fromString("Аналитика")
            else -> {
                Department.Android
            }
        }
    }
}