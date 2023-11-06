package com.example.myapplication.di

import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.domain.usecase.GetAllWorkingSubjectsUseCase
import com.example.myapplication.domain.usecase.GetAllWorkingSubjectsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @Provides
    fun getAllWorkingSubjectsUseCase(repository: TimeTrackerRepository): GetAllWorkingSubjectsUseCase {
        return GetAllWorkingSubjectsUseCaseImpl(repository)
    }
}