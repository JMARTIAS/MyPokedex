package com.example.mypokedex.domain.usecase

import com.example.mypokedex.domain.model.Pokemon
import com.example.mypokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritePokemonsUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    operator fun invoke(): Flow<List<Pokemon>> {
        return pokemonRepository.getFavoritePokemons()
    }

}
