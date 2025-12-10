package com.example.mypokedex.domain.usecase

import com.example.mypokedex.domain.model.Pokemon
import com.example.mypokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    suspend operator fun invoke(page: Int): Result<List<Pokemon>> {
        return pokemonRepository.getPokemonList(page)
    }

}
