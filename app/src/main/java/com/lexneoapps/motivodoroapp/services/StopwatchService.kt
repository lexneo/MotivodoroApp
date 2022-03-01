package com.lexneoapps.motivodoroapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.app.MainActivity
import com.lexneoapps.motivodoroapp.data.record.Record
import com.lexneoapps.motivodoroapp.data.record.RecordDao
import com.lexneoapps.motivodoroapp.other.Constants
import com.lexneoapps.motivodoroapp.util.formatMillisToTimer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchService : LifecycleService() {
    private lateinit var notificationManager: NotificationManager
    private var elapsedMillisBeforePause = 0L

    @Inject
    lateinit var recordDao: RecordDao


    override fun onCreate() {
        super.onCreate()

        setupNotification(SingletonProjectAttr.projectName)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                SERVICESTATE.START_OR_RESUME.name -> {
                    startResumeStopWatch()
                    startForeground(
                        NOTIFICATION_ID,
                        getNotification(
                            SingletonProjectAttr.projectName,
                            formatMillisToTimer(elapsedMillisBeforePause, false)
                        )
                    )
                }
                SERVICESTATE.PAUSE.name -> {
                    pauseStopWatch()
                }
                SERVICESTATE.RESET.name -> {
                    resetStopWatch()
                    stopForeground(true)
                    stopSelf()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startResumeStopWatch() {
        timeStarted = System.currentTimeMillis()
//        timeEnded = 0L
        _isTracking.value = true
        lifecycleScope.launch(Dispatchers.Default) {
            val startTimeMillis = System.currentTimeMillis()
            while (_isTracking.value!!) {
                _elapsedMilliSeconds.postValue((System.currentTimeMillis() - startTimeMillis) + elapsedMillisBeforePause)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(_elapsedMilliSeconds.value!!)
                if (_elapsedSeconds.value != seconds) {
                    _elapsedSeconds.postValue(seconds)
                }
                delay(100)
            }
        }
    }

    private fun pauseStopWatch() {
        timeEnded = System.currentTimeMillis()
        var totalTime = (timeEnded - timeStarted)
        lifecycleScope.launch {
            recordDao.insertRecord(
                Record(
                    SingletonProjectAttr.projectName,
                    timeStarted,
                    timeEnded,
                    totalTime,
                    SingletonProjectAttr.projectColor
                )
            )
        }
        _isTracking.value = false
        elapsedMillisBeforePause = _elapsedMilliSeconds.value!!
        notificationManager.notify(
            NOTIFICATION_ID,
            getNotification(
                SingletonProjectAttr.projectName,
                formatMillisToTimer(elapsedMillisBeforePause)
            )
        )
    }

    private fun resetStopWatch() {
        _isTracking.value = false
        _elapsedMilliSeconds.value = 0L
        _elapsedSeconds.value = 0L
        elapsedMillisBeforePause = 0L
        timeStarted = 0L
        timePaused = 0
        timeEnded = System.currentTimeMillis()
        lifecycleScope.coroutineContext.cancelChildren()
    }

    private fun setupNotification(string: String) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        _elapsedSeconds.observe(this) { elapsedSeconds ->
            if (_isTracking.value!!) {
                notificationManager.notify(
                    NOTIFICATION_ID,
                    getNotification(
                        string,
                        formatMillisToTimer(TimeUnit.SECONDS.toMillis(elapsedSeconds))
                    )
                )
            }
        }
    }

    private fun getNotification(title: String, text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setColor(SingletonProjectAttr.projectColor)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    1,
                    Intent(this, MainActivity::class.java).also {
                        it.action = Constants.ACTION_SHOW_STOPWATCH_FRAGMENT
                    },
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .addAction(
                R.drawable.ic_baseline_timer_24,
                if (_isTracking.value!!) getString(R.string.pause_stopwatch) else getString(R.string.resume_stopwatch),
                PendingIntent.getService(
                    this,
                    2,
                    Intent(
                        this,
                        StopwatchService::class.java
                    ).also {
                        it.action =
                            if (_isTracking.value!!) SERVICESTATE.PAUSE.name else SERVICESTATE.START_OR_RESUME.name
                    },
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .build()
    }

    companion object {
        const val NOTIFICATION_ID = 172
        const val NOTIFICATION_CHANNEL_ID = "473"
        const val NOTIFICATION_CHANNEL_NAME = "stopwatch_channel"

        private val _isTracking = MutableLiveData(false)
        val isTracking: LiveData<Boolean> = _isTracking

        private val _elapsedSeconds = MutableLiveData(0L)
        val elapsedSeconds: LiveData<Long> = _elapsedSeconds

        private val _elapsedMilliSeconds = MutableLiveData(0L)
        val elapsedMilliSeconds: LiveData<Long> = _elapsedMilliSeconds

        var timeStarted = 0L
        var currentTotaltime = 0L
        var timeEnded = 0L
        var timePaused = 0L
    }

    enum class SERVICESTATE {
        START_OR_RESUME,
        PAUSE,
        RESET
    }
}

object SingletonProjectAttr {
    var projectName = "StopWatch"
    var projectColor = -30808

    fun setName(name: String) {
        this.projectName = name
    }
}
