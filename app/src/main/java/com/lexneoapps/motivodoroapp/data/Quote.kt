package com.lexneoapps.motivodoroapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote_table")
data class Quote(
    var author: String = "",
    var theQuote: String = "",
    var favorite: Boolean = false,
    var showed: Boolean = false,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) {


}