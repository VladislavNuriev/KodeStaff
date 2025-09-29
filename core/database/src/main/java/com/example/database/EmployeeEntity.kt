package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey
    val id: String,
    val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val userTag: String,
    val department: String,
    val position: String,
    val birthday: Long,
    val phone: String
)
