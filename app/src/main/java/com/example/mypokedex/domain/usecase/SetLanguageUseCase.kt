package com.example.mypokedex.domain.usecase

import com.example.mypokedex.domain.repository.LanguageRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    suspend operator fun invoke(language: String) {
        repository.setLanguage(language)
    }
}
