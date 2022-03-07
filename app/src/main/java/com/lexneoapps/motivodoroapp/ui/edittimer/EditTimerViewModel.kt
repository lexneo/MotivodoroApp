package com.lexneoapps.motivodoroapp.ui.edittimer

import androidx.lifecycle.*
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimer
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimerDao
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditTimerViewModel @Inject constructor(

    private val cdTimerDao: CDTimerDao
) : ViewModel() {

    //cdtimer1
    val cdTimers = cdTimerDao.getTimers().asLiveData()


    private var _countdown = MutableLiveData<Int>()
    val countdown: LiveData<Int> = _countdown

    private var _pause = MutableLiveData<Int>()
    val pause: LiveData<Int> = _pause

    private var _repeat = MutableLiveData<Int>()
    val repeat: LiveData<Int> = _repeat

    private var _name = MutableLiveData<String>()
    val name: LiveData<String> = _name


    fun getCDTimerById(cdTimerID: Int) {
        viewModelScope.launch {
            val timer = cdTimerDao.getTimerById(cdTimerID)
            _countdown.value = timer.countdown
            _repeat.value = timer.repeat
            _pause.value = timer.pause
            _name.value = timer.name

            Timber.i("EditTimerViewModel ${timer.countdown} ::${_countdown.value} , ${timer.repeat}, ${timer.pause}")
        }
    }

    fun saveCDTimer(countdown: Int, pause: Int, repeat: Int, name: String, id: Int) {
        viewModelScope.launch {
            var cdTimer = CDTimer(countdown, pause, repeat,name,id)
            cdTimerDao.updateCDTimer(cdTimer)
        }

    }


}