package com.lexneoapps.motivodoroapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.lexneoapps.motivodoroapp.data.PreferencesManager.PreferencesKeys.IS_FIRST_OPEN_KEY
import com.lexneoapps.motivodoroapp.data.PreferencesManager.PreferencesKeys.QUOTE_ID
import com.lexneoapps.motivodoroapp.data.PreferencesManager.PreferencesKeys.UI_MODE_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.prefDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

enum class SortOrder { BY_RECENT, BY_TOTAL, BY_NAME }

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.prefDataStore

    val filterDataFlow: Flow<SortOrder>
        get() = dataStore.data.map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_RECENT.name
            )
            sortOrder
        }

    val uiMode: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            val uiMode = preferences[UI_MODE_KEY] ?: false
            uiMode
        }

    suspend fun updateNightMode(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[UI_MODE_KEY] = isNightMode
        }
    }


    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    val firstOpen: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            val firstOpen = preferences[IS_FIRST_OPEN_KEY] ?: false
            firstOpen
        }

    suspend fun isFirstOpen(isTrue: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_OPEN_KEY] = isTrue
        }
    }

    val quoteId: Flow<Int>
        get() = dataStore.data.map { preferences ->
            val quoteId = preferences[QUOTE_ID] ?: 4
            quoteId
        }

    suspend fun quoteId(id: Int) {
        dataStore.edit { preferences ->
            preferences[QUOTE_ID] = id
        }
    }

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val UI_MODE_KEY = booleanPreferencesKey("ui_mode")
        val IS_FIRST_OPEN_KEY = booleanPreferencesKey("IS_FIRST_OPEN_KEY")
        val QUOTE_ID = intPreferencesKey("QUOTE_ID")
    }
}