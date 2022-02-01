package com.lexneoapps.motivodoroapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import com.lexneoapps.motivodoroapp.data.quote.QuoteDao
import com.lexneoapps.motivodoroapp.data.record.RecordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatsAndQuotesViewModel @Inject constructor(
    private val projectDao: ProjectDao,
    private val quoteDao: QuoteDao,
    private val recordDao: RecordDao
) : ViewModel() {
}