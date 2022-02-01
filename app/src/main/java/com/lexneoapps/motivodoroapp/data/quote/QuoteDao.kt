package com.lexneoapps.motivodoroapp.data.quote

import androidx.room.*
import com.lexneoapps.motivodoroapp.data.quote.Quote

@Dao
interface QuoteDao {

    //for quotes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: Quote)

    @Delete
    suspend fun deleteQuote(quote: Quote)

    @Update
    suspend fun updateQuote(quote: Quote)

   /* @Query("SELECT * FROM quote_table WHERE favorite != 0")
    suspend fun favoriteQuote() : Flow<List<Quote>>*/
}