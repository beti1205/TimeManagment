package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.common.Database
import com.example.myapplication.common.MIGRATION_1_2
import com.example.myapplication.data.TimeTrackerRepository
import com.example.myapplication.data.TimeTrackerRepositoryImpl
import com.example.myapplication.timetracker.domain.usecases.GetLastWorkingSubjectsUseCase
import com.example.myapplication.timetracker.domain.usecases.GetLastWorkingSubjectsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database =
        Room.databaseBuilder(
            context,
            Database::class.java,
            "time_tracker_database"
        ).addMigrations(MIGRATION_1_2)
            .build()

    @Singleton
    @Provides
    fun provideTimeTrackerRepository(db: Database): TimeTrackerRepository {
        return TimeTrackerRepositoryImpl(db.timeTrackerDao())
    }

    @Provides
    fun getLastWorkingSubjectsUseCase(repository: TimeTrackerRepository): GetLastWorkingSubjectsUseCase {
        return GetLastWorkingSubjectsUseCaseImpl(repository)
    }
}