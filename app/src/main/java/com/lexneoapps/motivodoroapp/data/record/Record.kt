package com.lexneoapps.motivodoroapp.data.record

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lexneoapps.motivodoroapp.R
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "record_table")
@Parcelize
data class Record (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var projectName: String = "",
    var startTime: Long = System.currentTimeMillis(),
    var endTime: Long = System.currentTimeMillis(),
    var totalTime: Long = 0L,
    var projectColor: Int = R.color.primaryColor

    ) : Parcelable {

    val startTimeFormatted: String
        get() = DateFormat.getDateTimeInstance().format(startTime)

    val endTimeTimeFormatted: String
        get() = DateFormat.getDateTimeInstance().format(endTime)
}