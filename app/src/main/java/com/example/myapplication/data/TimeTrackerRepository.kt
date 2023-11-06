package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TimeTrackerRepository {

    suspend fun insert(timeTrackerEntity: TimeTrackerEntity)
    fun getAllWorkingSubject(): Flow<List<String>>
    fun getAllTimeTrackerEntity(): Flow<List<TimeTrackerEntity>>
    fun getLastWorkingSubject(): Flow<String>
}

class TimeTrackerRepositoryImpl @Inject constructor(
    private val timeTrackerDao: TimeTrackerDao
) : TimeTrackerRepository {
    override suspend fun insert(timeTrackerEntity: TimeTrackerEntity) {
        timeTrackerDao.insert(timeTrackerEntity)
    }

    override fun getAllWorkingSubject(): Flow<List<String>> {
        return timeTrackerDao.getAllWorkingSubjects()
    }

    override fun getAllTimeTrackerEntity(): Flow<List<TimeTrackerEntity>> {
        return timeTrackerDao.getAllTimeTrackerEntity()
    }

    override fun getLastWorkingSubject(): Flow<String> {
        return timeTrackerDao.getLastWorkingSubject()
    }
}