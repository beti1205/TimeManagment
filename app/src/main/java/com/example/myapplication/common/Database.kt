package com.example.myapplication.common

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.TimeTrackerDao
import com.example.myapplication.data.TimeTrackerEntity

@Database(
    entities = [TimeTrackerEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(InstantLongConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun timeTrackerDao(): TimeTrackerDao
}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create the new table
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS TimeTrackerEntityTmp (id INTEGER PRIMARY" +
                    " KEY AUTOINCREMENT NOT NULL, timeElapsed INTEGER NOT NULL, startTime" +
                    " INTEGER NULL, endTime INTEGER NULL, workingSubject TEXT NOT NULL )"
        )

        // Copy the data
        database.execSQL(
            "INSERT INTO TimeTrackerEntityTmp (id, timeElapsed, startTime, " +
                    "endTime, workingSubject) SELECT id, timeElapsed, startTime, endTime," +
                    " workingSubject FROM TimeTrackerEntity")

        // Remove the old table
        database.execSQL("DROP TABLE TimeTrackerEntity")

        // Change the table name to the correct one
        database.execSQL("ALTER TABLE TimeTrackerEntityTmp RENAME TO TimeTrackerEntity")
    }
}
