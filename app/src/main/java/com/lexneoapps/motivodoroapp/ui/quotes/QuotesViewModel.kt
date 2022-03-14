package com.lexneoapps.motivodoroapp.ui.quotes

import androidx.lifecycle.*
import com.lexneoapps.motivodoroapp.data.quote.Quote
import com.lexneoapps.motivodoroapp.data.quote.QuoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuotesViewModel @Inject constructor(
    private val quoteDao: QuoteDao
) : ViewModel() {


    val unlockedQuotes = quoteDao.getUnlockedQuotes().asLiveData()

    val favoriteQuotes = quoteDao.getFavoriteQuotes().asLiveData()


    private var _numberOfUnlockedQuotes = MutableLiveData<Int>()
    val numberOfUnlockedQuotes: LiveData<Int> = _numberOfUnlockedQuotes



    fun setFavorite(currentQuote: Quote, isFavorite: Boolean) =
        viewModelScope.launch {
            Timber.i("isSFavorite is $isFavorite")
            quoteDao.updateQuote(currentQuote.copy(favorite = isFavorite))
        }


}