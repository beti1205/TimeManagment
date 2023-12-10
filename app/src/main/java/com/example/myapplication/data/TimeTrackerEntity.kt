package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.common.InstantLongConverter
import java.time.Instant

@Entity
@TypeConverters(InstantLongConverter::class)
data class TimeTrackerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: Instant?,
    val endTime: Instant?,
    val workingSubject: String,
    val date: String
)
