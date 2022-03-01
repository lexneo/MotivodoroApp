package com.lexneoapps.motivodoroapp.data.record

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.util.formatMillisToTimer
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "record_table")
@Parcelize
data class Record (

    var projectName: String = "",
    var startTime: Long = System.currentTimeMillis(),
    var endTime: Long = System.currentTimeMillis(),
    var totalTime: Long = 0L,
    var projectColor: Int = -30080,
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    ) : Parcelable {

    //get date started
    val startTimeFormattedDate: String
        get() = DateFormat.getDateInstance().format(startTime)

    val endTimeTimeFormattedDate: String
        get() = DateFormat.getDateTimeInstance().format(endTime)


    //get time started
    val startTimeFormattedTime: String
        get() = DateFormat.getTimeInstance().format(startTime)

    val endTimeTimeFormattedTime: String
        get() = DateFormat.getTimeInstance().format(endTime)


    val totalTimeFormatted: String
        get() = formatMillisToTimer(totalTime)



}