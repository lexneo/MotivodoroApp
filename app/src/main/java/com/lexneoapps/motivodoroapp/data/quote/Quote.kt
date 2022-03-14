package com.lexneoapps.motivodoroapp.data.quote

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "quote_table")
@Parcelize
data class Quote(
    var author: String = "",
    var theQuote: String = "",
    var favorite: Boolean = false,
    var showed: Boolean = false,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable{




}