package com.example.mypokedex.domain.repository

import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    fun getLanguage(): Flow<String>
    suspend fun setLanguage(language: String)
}
