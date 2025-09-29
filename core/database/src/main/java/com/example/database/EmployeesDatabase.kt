package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [EmployeeEntity::class],
    version = 2,
    exportSchema = false
)
abstract class EmployeesDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
}