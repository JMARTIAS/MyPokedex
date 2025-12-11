package com.example.mypokedex.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mypokedex.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LanguageRepository {

    private object PreferencesKeys {
        val LANGUAGE = stringPreferencesKey("language")
    }

    override fun getLanguage(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.LANGUAGE] ?: Locale.getDefault().language
        }
    }

    override suspend fun setLanguage(language: String) {
        dataStore.edit {
            it[PreferencesKeys.LANGUAGE] = language
        }
    }
}
