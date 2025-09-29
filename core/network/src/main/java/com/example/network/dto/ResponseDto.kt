package com.example.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseDto(
    @SerializedName("items")
    @Expose
    val employees: List<EmployeeDto>
)
