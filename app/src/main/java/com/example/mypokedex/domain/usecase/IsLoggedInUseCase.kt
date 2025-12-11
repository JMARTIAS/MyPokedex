package com.example.mypokedex.domain.usecase

import com.example.mypokedex.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<Boolean> {
        return userRepository.isLoggedIn()
    }

}
