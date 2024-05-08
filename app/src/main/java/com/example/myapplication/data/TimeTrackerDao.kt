package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface TimeTrackerDao {

    @Insert
    suspend fun insert(timeTrackerEntity: TimeTrackerEntity)

    @Update
    suspend fun updateTimeInterval(timeTrackerEntity: TimeTrackerEntity)

    @Query("SELECT workingSubject FROM timetrackerentity")
    fun getAllWorkingSubjects(): Flow<List<String>>

    @Query("SELECT * FROM timetrackerentity ORDER BY id DESC ")
    fun getAllTimeTrackerEntity(): Flow<List<TimeTrackerEntity>>

    @Query("SELECT workingSubject FROM timetrackerentity WHERE id=(SELECT max(id) FROM timetrackerentity)")
    fun getLastWorkingSubject(): Flow<String>

    @Query("DELETE FROM timetrackerentity WHERE id = :id")
    suspend fun deleteTimeInterval(id: Int)

    @Query("UPDATE timetrackerentity SET workingSubject = :subject, startTime = :startTime, endTime = :endTime WHERE id = :id ")
    suspend fun updateTimeInterval(id: Int, subject: String, startTime: Instant, endTime: Instant)
}