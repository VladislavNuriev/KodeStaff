package com.example.employees.models

import com.example.util.DateUtils
import com.example.data.models.Employee
import javax.inject.Inject

class MapperPresentation @Inject constructor() {
    private fun employeeModelToEmployeeUi(model: Employee): EmployeeUi {
        with(model) {
            return EmployeeUi(
                id = id,
                avatarUrl = avatarUrl,
                firstName = firstName,
                lastName = lastName,
                userTag = userTag,
                department = department,
                position = position,
                birthday = DateUtils.timeStampToDayMonth(birthday),
                phone = phone
            )
        }
    }


    fun modelListToUiList(models: List<Employee>): List<EmployeeUi> {
        return models.map {
            employeeModelToEmployeeUi(it)
        }
    }
}