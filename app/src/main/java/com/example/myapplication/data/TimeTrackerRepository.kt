package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject

interface TimeTrackerRepository {

    suspend fun insert(timeTrackerEntity: TimeTrackerEntity)
    fun getAllWorkingSubject(): Flow<List<String>>
    fun getAllTimeTrackerEntity(): Flow<List<TimeTrackerEntity>>
    fun getLastWorkingSubject(): Flow<String>
    suspend fun deleteTimeInterval(id: Int)
    suspend fun updateTimeInterval(id: Int, subject: String, startTime: Instant, endTime: Instant)

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

    override suspend fun deleteTimeInterval(id: Int) {
        return timeTrackerDao.deleteTimeInterval(id)
    }

    override suspend fun updateTimeInterval(id: Int, subject: String, startTime: Instant, endTime: Instant) {
        return timeTrackerDao.updateTimeInterval(
            id = id,
            subject = subject,
            startTime = startTime,
            endTime = endTime
        )
    }
}