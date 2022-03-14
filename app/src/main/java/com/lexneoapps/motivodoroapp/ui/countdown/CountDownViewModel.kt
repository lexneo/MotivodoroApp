package com.lexneoapps.motivodoroapp.ui.countdown

import androidx.lifecycle.*
import com.lexneoapps.motivodoroapp.data.PreferencesManager
import com.lexneoapps.motivodoroapp.data.quote.Quote
import com.lexneoapps.motivodoroapp.data.quote.QuoteDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CountDownViewModel @Inject constructor(
    private val quoteDao: QuoteDao,
    private val preferencesManager: PreferencesManager,
    ) : ViewModel() {





    private lateinit var allQuotes: List<Quote>
    private var _vmInitialized = MutableLiveData<Boolean>()
    val vmInitialized: LiveData<Boolean> = _vmInitialized



    init {
        viewModelScope.launch {
            allQuotes = quoteDao.getAll()
            delay(500)
            val getQuoteId = preferencesManager.quoteId.first()
            _currentQuote.value = allQuotes[getQuoteId]

            _vmInitialized.value = true

        }
    }





    fun setToFirstOpen(isTrue: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.isFirstOpen(isTrue)
        }
    }

    private var _randomQuote = MutableLiveData<Quote>()
    val randomQuote: LiveData<Quote> = _randomQuote

    private var _quoteText = MutableLiveData<String>()
    val quoteText: LiveData<String> = _quoteText

    private var _quoteAuthor = MutableLiveData<String>()
    val quoteAuthor: LiveData<String> = _quoteAuthor

    private var _quoteFavorite = MutableLiveData<Boolean>()
    val quoteFavorite: LiveData<Boolean> = _quoteFavorite

    private var _quoteId = MutableLiveData<Int>()
    val quoteId: LiveData<Int> = _quoteId


    private var _currentQuote = MutableLiveData<Quote>()
    val currentQuote: LiveData<Quote> = _currentQuote


    fun getRandomQuote() {
        val random = Random.nextInt(allQuotes.size)
        val quote = allQuotes[random]
        _currentQuote.value = quote
        _quoteAuthor.value = quote.author
        _quoteText.value = quote.theQuote
        _quoteFavorite.value = quote.favorite
        _quoteId.value = quote.id
        viewModelScope.launch {
            preferencesManager.quoteId(quote.id)
            quoteDao.updateQuote(quote.copy(showed = true))

        }
    }

    fun setFavorite( isFavorite: Boolean) =
        viewModelScope.launch {
            Timber.i("isSFavorite is $isFavorite")
            _currentQuote.value?.let { quoteDao.updateQuote(it.copy(favorite = isFavorite)) }
        }

}



