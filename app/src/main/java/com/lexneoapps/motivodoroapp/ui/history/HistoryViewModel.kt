package com.lexneoapps.motivodoroapp.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lexneoapps.motivodoroapp.data.record.RecordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    recordDao: RecordDao
) : ViewModel(){

    val getRecords = recordDao.getRecords().asLiveData()

}