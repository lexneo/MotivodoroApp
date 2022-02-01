package com.lexneoapps.motivodoroapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimer
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimerDao
import com.lexneoapps.motivodoroapp.data.project.Project
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import com.lexneoapps.motivodoroapp.data.quote.Quote
import com.lexneoapps.motivodoroapp.data.quote.QuoteDao
import com.lexneoapps.motivodoroapp.data.record.Record
import com.lexneoapps.motivodoroapp.data.record.RecordDao
import com.lexneoapps.motivodoroapp.other.Constants.APP_DATABASE_NAME

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

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    APP_DATABASE_NAME
                ).createFromAsset("database/appdatabase.db")
                    .build()

                INSTANCE = instance
                return instance

            }
        }
    }

}