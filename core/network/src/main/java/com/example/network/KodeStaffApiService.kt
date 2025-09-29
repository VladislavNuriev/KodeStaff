package com.example.network

import com.example.network.dto.ResponseDto
import retrofit2.http.GET
import retrofit2.http.Headers

interface KodeStaffApiService {

    @GET("users")
    @Headers(
        "Accept: application/json, application/xml",
        "Prefer: code=200, example=success"
    )
    suspend fun getEmployees(): ResponseDto
}