package com.lexneoapps.motivodoroapp.ui.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimer
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimerDao
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    cdTimerDao: CDTimerDao
) : ViewModel() {


    val cdTimers = cdTimerDao.getTimers().asLiveData()
}