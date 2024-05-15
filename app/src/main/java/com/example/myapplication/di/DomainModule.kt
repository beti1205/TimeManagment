package com.example.myapplication.di

import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.timesheet.domain.usecases.AddTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.AddTimeIntervalUseCaseImpl
import com.example.myapplication.timesheet.domain.usecases.DateValidator
import com.example.myapplication.timesheet.domain.usecases.DateValidatorImpl
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.DeleteTimeIntervalUseCaseImpl
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCase
import com.example.myapplication.timesheet.domain.usecases.GetTimeTrackerIntervalsUseCaseImpl
import com.example.myapplication.timesheet.domain.usecases.ValidateTime
import com.example.myapplication.timesheet.domain.usecases.ValidateTimeImpl
import com.example.myapplication.timesheet.domain.usecases.UpdateTimeIntervalUseCase
import com.example.myapplication.timesheet.domain.usecases.UpdateTimeIntervalUseCaseImpl
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
    fun getTimeTrackerIntervalsUseCase(repository: TimeTrackerRepository): GetTimeTrackerIntervalsUseCase {
        return GetTimeTrackerIntervalsUseCaseImpl(repository)
    }

    @Provides
    fun deleteTimeIntervalUseCase(repository: TimeTrackerRepository): DeleteTimeIntervalUseCase {
        return DeleteTimeIntervalUseCaseImpl(repository)
    }

    @Provides
    fun updateTimeIntervalUseCase(repository: TimeTrackerRepository): UpdateTimeIntervalUseCase {
        return UpdateTimeIntervalUseCaseImpl(repository)
    }

    @Provides
    fun addTimeIntervalUseCase(repository: TimeTrackerRepository): AddTimeIntervalUseCase {
        return AddTimeIntervalUseCaseImpl(repository)
    }

    @Provides
    fun timeValidator(): ValidateTime {
        return ValidateTimeImpl()
    }

    @Provides
    fun dateValidator(): DateValidator {
        return DateValidatorImpl()
    }
}