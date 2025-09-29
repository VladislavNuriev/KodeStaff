package com.example.data.di

import com.example.data.EmployeesRepository
import com.example.data.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindEmployeesRepository(repository: RepositoryImpl): EmployeesRepository
}