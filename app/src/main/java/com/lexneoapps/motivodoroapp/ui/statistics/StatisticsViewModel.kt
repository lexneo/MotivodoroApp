package com.lexneoapps.motivodoroapp.ui.statistics

import androidx.lifecycle.*
import com.github.mikephil.charting.data.PieEntry
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import com.lexneoapps.motivodoroapp.data.record.RecordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val recordDao: RecordDao,
    private val projectDao: ProjectDao
) : ViewModel() {




    val allProjects = projectDao.getAllProjects().asLiveData()



}