package com.lexneoapps.motivodoroapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Project::class,
        Record::class,
        Quote::class,
        CDTimer::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

        abstract fun projectDao(): ProjectDao
        abstract fun recordDao(): RecordDao
        abstract fun quoteDao(): QuoteDao
        abstract fun cdTimerDao(): CDTimerDao

}