package com.lexneoapps.motivodoroapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
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
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class CountdownService : LifecycleService() {
    private lateinit var notificationManager: NotificationManager
    private var elapsedMillisBeforePause = 0L
    private var timeLeft = Countdown.pomodoro * 1000L
    private lateinit var timer: Job


    @Inject
    lateinit var recordDao: RecordDao


    override fun onCreate() {
        super.onCreate()

        setupNotification(SingletonProjectAttr.projectName)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                CDSERVICESTATE.START_OR_RESUME.name -> {
                    _isOver.value = false

                    startResumeStopWatch()
                    startForeground(
                        NOTIFICATION_ID_CD,
                        getNotification(
                            SingletonProjectAttr.projectName,
                            formatMillisToTimer(elapsedMillisBeforePause, false)
                        )
                    )
                }
                CDSERVICESTATE.PAUSE.name -> {
                    pauseStopWatch()
                }
                CDSERVICESTATE.RESET.name -> {
                    resetStopWatch()
                    stopForeground(true)
                    stopSelf()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startResumeStopWatch() {
        _roundsLeft.value = Countdown.rounds
        _isStarted.value = true
        _isOver.value = false


        timeStartedCD = System.currentTimeMillis()

        _isTrackingCD.value = true
        _timerReachedZero.value = false

        timer = lifecycleScope.launch(Dispatchers.Main) {

            var timeToCountdown = Countdown.pomodoro * 1000L  + 1000L
//            var timeToCountdown = Countdown.pomodoro * 1000L * 60L + 1000L
            if (isBreakMode) timeToCountdown = Countdown.shortBreak * 1000L + 1000L
//            if (isBreakMode) timeToCountdown = Countdown.shortBreak * 1000L * 60L + 1000L
            val startTimeMillis = System.currentTimeMillis()
            while (_isTrackingCD.value!!) {


                _elapsedMilliSecondsCD.postValue((System.currentTimeMillis() - startTimeMillis) + elapsedMillisBeforePause)
                _timeLeftMilliSecondsCD.postValue((timeToCountdown - ((System.currentTimeMillis() - startTimeMillis) + elapsedMillisBeforePause)))
                val seconds = TimeUnit.MILLISECONDS.toSeconds(_elapsedMilliSecondsCD.value!!)
                val leftSeconds =
                    TimeUnit.MILLISECONDS.toSeconds(_timeLeftMilliSecondsCD.value!!)
                if (_elapsedSecondsCD.value != seconds) {
                    _elapsedSecondsCD.postValue(seconds)
                }

                if (_timeLeftInSecondsCD.value != leftSeconds) {
                    if (leftSeconds == 0L && !isBreakMode) {
                        reachedZero = true
                        _timerReachedZero.postValue(true)
//

                        launch {
                            timerOver()
                        }


                    } else if (leftSeconds == 0L && isBreakMode) {
                        launch {
                            timerOver()
                        }
                    }

                    _timeLeftInSecondsCD.postValue(leftSeconds)



                }

                if (this.isActive) {
                    delay(50)

                }



            }
        }
    }

    private fun saveRecord() {
        timeEndedCD = System.currentTimeMillis()
        var totalTime = (timeEndedCD - timeStartedCD)
        if (!isBreakMode) {
            lifecycleScope.launch {
                recordDao.insertRecord(
                    Record(
                        SingletonProjectAttr.projectName,
                        timeStartedCD,
                        timeEndedCD,
                        totalTime,
                        SingletonProjectAttr.projectColor
                    )
                )
            }
        }
    }

    private fun pauseStopWatch() {
        timeEndedCD = System.currentTimeMillis()
        var totalTime = (timeEndedCD - timeStartedCD)
        saveRecord()

        _isTrackingCD.value = false
        timeLeft = _timeLeftMilliSecondsCD.value!!
        elapsedMillisBeforePause = _elapsedMilliSecondsCD.value!!

        notificationManager.notify(
            NOTIFICATION_ID_CD,
            getNotification(
                SingletonProjectAttr.projectName + " (Paused)",
                formatMillisToTimer(timeLeft)
            )
        )
    }

    private suspend fun timerOver() {
        _isTrackingCD.value = false

        saveRecord()


        isBreakMode = !isBreakMode
        _breakProgress.value = !_breakProgress.value!!

        timer.cancel()
        if (!isBreakMode) {

            Countdown.rounds--
            _roundsLeft.value = Countdown.rounds


        }
        if (Countdown.rounds > 0) {

            _elapsedMilliSecondsCD.value = 0L
            _elapsedSecondsCD.value = 0L
            elapsedMillisBeforePause = 0L

            startResumeStopWatch()

        } else {

            resetStopWatch()
            stopForeground(true)
            stopSelf()
        }

    }

    private fun resetStopWatch() {
        _isStarted.value = false
        _isOver.value = true
        _isTrackingCD.value = false
        _elapsedMilliSecondsCD.value = 0L
        _elapsedSecondsCD.value = 0L
        elapsedMillisBeforePause = 0L
        timeStartedCD = 0L
        timePausedCD = 0
        timeEndedCD = System.currentTimeMillis()
        lifecycleScope.coroutineContext.cancelChildren()
    }

    private fun setupNotification(string: String) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

         _timeLeftInSecondsCD.observe(this) { timeLeftSeconds ->
            if (_isTrackingCD.value!!) {
                if (isBreakMode) {
                    notificationManager.notify(
                        NOTIFICATION_ID_CD,
                        getNotification(
                            "$string  BREAK",
                            formatMillisToTimer(TimeUnit.SECONDS.toMillis(timeLeftSeconds))
                        )
                    )
                } else {
                    notificationManager.notify(
                        NOTIFICATION_ID_CD,
                        getNotification(
                            "$string  ACTIVE",
                            formatMillisToTimer(TimeUnit.SECONDS.toMillis(timeLeftSeconds))
                        )
                    )
                }

            }
        }
    }

    private fun getNotification(title: String, text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID_CD,
                NOTIFICATION_CHANNEL_NAME_CD,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_CD)
            .setOngoing(true)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setColor(SingletonProjectAttr.projectColor)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentText(text)
            .setOnlyAlertOnce(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    3,
                    Intent(this, MainActivity::class.java).also {
                        it.action = Constants.ACTION_SHOW_COUNTDOWN_FRAGMENT
                    },
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .addAction(
                R.drawable.ic_baseline_timer_24,
                if (_isTrackingCD.value!!) getString(R.string.pause_stopwatch) else getString(R.string.resume_stopwatch),
                PendingIntent.getService(
                    this,
                    4,
                    Intent(
                        this,
                        CountdownService::class.java
                    ).also {
                        it.action =
                            if (_isTrackingCD.value!!) CDSERVICESTATE.PAUSE.name else CDSERVICESTATE.START_OR_RESUME.name
                    },
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .build()
    }

    companion object {
        const val NOTIFICATION_ID_CD = 86
        const val NOTIFICATION_CHANNEL_ID_CD = "236"
        const val NOTIFICATION_CHANNEL_NAME_CD = "COUNTDOWN_CHANNEL"

        private val _isTrackingCD = MutableLiveData(false)
        val isTrackingCD: LiveData<Boolean> = _isTrackingCD

        private val _elapsedSecondsCD = MutableLiveData(0L)
        val elapsedSecondsCD: LiveData<Long> = _elapsedSecondsCD

        private val _elapsedMilliSecondsCD = MutableLiveData(0L)
        val elapsedMilliSecondsCD: LiveData<Long> = _elapsedMilliSecondsCD

        private var _timeLeftMilliSecondsCD = MutableLiveData(0L)
        val timeLeftMilliSecondsCD: LiveData<Long> = _timeLeftMilliSecondsCD

        private var _timeLeftInSecondsCD = MutableLiveData(0L)
        val timeLeftInSecondsCD: LiveData<Long> = _timeLeftInSecondsCD

        var reachedZero = false

        var isBreakMode = false
        var _timerReachedZero = MutableLiveData(false)
        val timerReachedZero: LiveData<Boolean> = _timerReachedZero

        private var _roundsLeft = MutableLiveData<Int>()
        val roundsLeft: LiveData<Int> = _roundsLeft

        private var _breakProgress = MutableLiveData(false)
        val breakProgress: LiveData<Boolean> = _breakProgress

        private var _isStarted = MutableLiveData(false)
        val isStarted : LiveData<Boolean> = _isStarted

         var _isOver = MutableLiveData(false)
        val isOver : LiveData<Boolean> = _isOver

        var timeStartedCD = 0L
        var currentTotaltimeCD = 0L
        var timeEndedCD = 0L
        var timePausedCD = 0L
    }

    enum class CDSERVICESTATE {
        START_OR_RESUME,
        PAUSE,
        RESET
    }
}




object Countdown{
    var pomodoro = 25
    var rounds = 4
    var shortBreak = 5

    fun setCountDown(pomodoro: Int,rounds: Int,shortBreak: Int){
        this.pomodoro = pomodoro
        this.rounds = rounds
        this.shortBreak = shortBreak
    }
}