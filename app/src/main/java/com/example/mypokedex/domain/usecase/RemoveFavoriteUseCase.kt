package com.example.mypokedex.domain.usecase

import com.example.mypokedex.domain.model.PokemonDetail
import com.example.mypokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    suspend operator fun invoke(pokemon: PokemonDetail) {
        pokemonRepository.removeFavorite(pokemon)
    }

}
