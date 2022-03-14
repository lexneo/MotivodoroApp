package com.lexneoapps.motivodoroapp.data.quote

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lexneoapps.motivodoroapp.data.quote.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    //for quotes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: Quote)

    @Delete
    suspend fun deleteQuote(quote: Quote)

    @Update
    suspend fun updateQuote(quote: Quote)

    @Query("SELECT * FROM quote_table WHERE  showed = 1")
    fun getUnlockedQuotes() : Flow<List<Quote>>

    @Query("SELECT * FROM quote_table")
    suspend fun getAll() : List<Quote>


    @Query("SELECT * FROM quote_table WHERE favorite = 1")
    fun getFavoriteQuotes() : Flow<List<Quote>>


}