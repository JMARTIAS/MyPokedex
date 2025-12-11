package com.example.mypokedex.domain.usecase

import com.example.mypokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    operator fun invoke(id: Int): Flow<Boolean> {
        return pokemonRepository.isFavorite(id)
    }

}
