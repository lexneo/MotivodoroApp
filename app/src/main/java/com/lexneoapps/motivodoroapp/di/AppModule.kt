package com.lexneoapps.motivodoroapp.di

import android.content.Context
import androidx.room.Room
import com.lexneoapps.motivodoroapp.data.AppDatabase
import com.lexneoapps.motivodoroapp.other.Constants.APP_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context,
    ) = Room.databaseBuilder(app, AppDatabase::class.java, APP_DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .createFromAsset("database/appdatabase.db")
        .build()

    @Provides
    fun providesProjectDao(db: AppDatabase) = db.projectDao()

    @Provides
    fun providesRecordDao(db: AppDatabase) = db.recordDao()

    @Provides
    fun providesCDTimerDao(db: AppDatabase) = db.cdTimerDao()

    @Provides
    fun providesQuoteDao(db: AppDatabase) = db.quoteDao()
}