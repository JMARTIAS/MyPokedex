package com.example.mypokedex.domain.usecase

import com.example.mypokedex.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    operator fun invoke(): Flow<String> {
        return repository.getLanguage()
    }
}
