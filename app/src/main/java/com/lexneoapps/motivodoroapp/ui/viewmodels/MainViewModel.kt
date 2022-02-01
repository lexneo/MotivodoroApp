package com.lexneoapps.motivodoroapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimerDao
import com.lexneoapps.motivodoroapp.data.PreferencesManager
import com.lexneoapps.motivodoroapp.data.SortOrder
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import com.lexneoapps.motivodoroapp.data.quote.QuoteDao
import com.lexneoapps.motivodoroapp.data.record.RecordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val projectDao: ProjectDao,
    private val cdTimerDao: CDTimerDao,
    private val quoteDao: QuoteDao,
    private val recordDao: RecordDao
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

//    val sortOrder = MutableStateFlow(SortOrder.BY_RECENT)

    private val sortOrderFlow = preferencesManager.filterDataFlow

    // get searchQuery and sortOrder combination for search
    private val projectFlow = combine(
        searchQuery,
        sortOrderFlow
    ) { query, sortOrder ->
        Pair(query, sortOrder)
    }
        .flatMapLatest {
            projectDao.getProjects(it.first, it.second)
        }

    // get UI mode
    val getUIMode = preferencesManager.uiMode

    // save UI mode
    fun saveToDataStore(isNightMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.updateNightMode(isNightMode)
        }
    }

    val projects = projectFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }



}

