package com.lexneoapps.motivodoroapp.ui.startandtimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimer
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimerDao
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectDao: ProjectDao,
    private val cdTimerDao: CDTimerDao
) : ViewModel() {

    //cdtimer1
    val cdTimers = cdTimerDao.getTimers().asLiveData()
}