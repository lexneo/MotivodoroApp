package com.lexneoapps.motivodoroapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lexneoapps.motivodoroapp.data.CDTimerDao
import com.lexneoapps.motivodoroapp.data.ProjectDao
import com.lexneoapps.motivodoroapp.data.QuoteDao
import com.lexneoapps.motivodoroapp.data.RecordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val projectDao: ProjectDao,
    private val cdTimerDao: CDTimerDao,
    private val quoteDao: QuoteDao,
    private val recordDao: RecordDao
) : ViewModel() {

    val projects = projectDao.getProjects().asLiveData()



}