package com.example.myapplication.di

import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCaseImpl
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCase
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCaseImpl
import com.example.myapplication.timetracker.domain.usecases.GetAllWorkingSubjectsUseCase
import com.example.myapplication.timetracker.domain.usecases.GetAllWorkingSubjectsUseCaseImpl
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

    @Provides
    fun getTimeTrackerIntervalsUseCase(repository: TimeTrackerRepository): GetTimeTrackerIntervalsUseCase{
        return GetTimeTrackerIntervalsUseCaseImpl(repository)
    }

    @Provides
    fun deleteTimeIntervalUseCase(repository: TimeTrackerRepository): DeleteTimeIntervalUseCase{
        return DeleteTimeIntervalUseCaseImpl(repository)
    }
}