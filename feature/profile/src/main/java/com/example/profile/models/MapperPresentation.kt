package com.example.profile.models

import com.example.data.models.Employee
import com.example.util.DateUtils
import javax.inject.Inject

class MapperPresentation @Inject constructor() {
    fun employeeModelToEmployeeUi(model: Employee): EmployeeUi {
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
}