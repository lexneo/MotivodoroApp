package com.lexneoapps.motivodoroapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lexneoapps.motivodoroapp.data.CDTimerDao
import com.lexneoapps.motivodoroapp.data.ProjectDao
import com.lexneoapps.motivodoroapp.data.QuoteDao
import com.lexneoapps.motivodoroapp.data.RecordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val projectDao: ProjectDao,
    private val cdTimerDao: CDTimerDao,
    private val quoteDao: QuoteDao,
    private val recordDao: RecordDao
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val sortOrder = MutableStateFlow(SortOrder.BY_RECENT)

    private val projectFlow = combine(
        searchQuery,
        sortOrder
    ){
        query,sortOrder ->
        Pair(query,sortOrder)
    }

        .flatMapLatest {
        projectDao.getProjects(it.first,it.second)
    }
    val projects = projectFlow.asLiveData()



}

enum class SortOrder {BY_RECENT,BY_TOTAL,BY_NAME}
