package com.example.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface EmployeeDao {

    @Insert(entity = EmployeeEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employees: List<EmployeeEntity>)

    @Query("""
    SELECT * FROM employees 
    WHERE 
        (:department = '' OR department = :department)
        AND (
            firstName LIKE '%' || :searchQuery || '%' 
            OR lastName LIKE '%' || :searchQuery || '%' 
            OR userTag LIKE '%' || :searchQuery || '%'
        )
    """)

    fun getEmployees(searchQuery: String = "", department: String = ""): Flow<List<EmployeeEntity>>
}