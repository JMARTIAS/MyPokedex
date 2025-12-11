package com.example.mypokedex.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun login(username: String)

    suspend fun logout()

    fun isLoggedIn(): Flow<Boolean>

}
