package com.example.mypokedex.domain.usecase

import com.example.mypokedex.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(username: String) {
        userRepository.login(username)
    }

}
