package com.lexneoapps.motivodoroapp.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import com.lexneoapps.motivodoroapp.data.record.RecordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val recordDao: RecordDao,
    private val projectDao: ProjectDao
) : ViewModel() {




    val allProjects = projectDao.getAllProjects().asLiveData()



}