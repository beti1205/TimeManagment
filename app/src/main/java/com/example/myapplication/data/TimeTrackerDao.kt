package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTrackerDao {

    @Insert
    suspend fun insert(timeTrackerEntity: TimeTrackerEntity)

    @Query("SELECT workingSubject FROM timetrackerentity")
    fun getAllWorkingSubjects(): Flow<List<String>>

    @Query("SELECT * FROM timetrackerentity ORDER BY id DESC ")
    fun getAllTimeTrackerEntity(): Flow<List<TimeTrackerEntity>>

    @Query("SELECT workingSubject FROM timetrackerentity WHERE id=(SELECT max(id) FROM timetrackerentity)")
    fun getLastWorkingSubject(): Flow<String>

    @Query("DELETE FROM timetrackerentity WHERE id = :id")
    suspend fun deleteTimeInterval(id: Int)
}