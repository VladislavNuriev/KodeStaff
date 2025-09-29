package com.example.database.di

import android.app.Application
import androidx.room.Room
import com.example.database.EmployeeDao
import com.example.database.EmployeesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): EmployeesDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = EmployeesDatabase::class.java,
            name = "employees.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration(true)
            .build()
    }


    @Provides
    @Singleton
    fun provideEmployeeDao(employeesDatabase: EmployeesDatabase): EmployeeDao {
        return employeesDatabase.employeeDao()
    }
}