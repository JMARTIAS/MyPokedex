package com.example.mypokedex.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mypokedex.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
    }

    override suspend fun login(username: String) {
        dataStore.edit {
            it[PreferencesKeys.USERNAME] = username
        }
    }

    override suspend fun logout() {
        dataStore.edit {
            it.remove(PreferencesKeys.USERNAME)
        }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map {
            it.contains(PreferencesKeys.USERNAME)
        }
    }

}
