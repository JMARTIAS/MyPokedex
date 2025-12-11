package com.example.mypokedex.domain.repository

import com.example.mypokedex.domain.model.Pokemon
import com.example.mypokedex.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemonList(page: Int): Result<List<Pokemon>>

    suspend fun getPokemonDetail(name: String): Result<PokemonDetail>

    fun getFavoritePokemons(): Flow<List<Pokemon>>

    suspend fun addFavorite(pokemon: PokemonDetail)

    suspend fun removeFavorite(pokemon: PokemonDetail)

    fun isFavorite(id: Int): Flow<Boolean>

}
