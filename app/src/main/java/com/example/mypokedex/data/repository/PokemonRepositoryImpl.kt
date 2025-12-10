package com.example.mypokedex.data.repository

import com.example.mypokedex.data.local.FavoritePokemonDao
import com.example.mypokedex.data.mapper.toFavoritePokemonEntity
import com.example.mypokedex.data.mapper.toPokemon
import com.example.mypokedex.data.mapper.toPokemonDetail
import com.example.mypokedex.data.remote.PokeApi
import com.example.mypokedex.domain.model.Pokemon
import com.example.mypokedex.domain.model.PokemonDetail
import com.example.mypokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi,
    private val favoritePokemonDao: FavoritePokemonDao
) : PokemonRepository {

    override suspend fun getPokemonList(page: Int): Result<List<Pokemon>> {
        return try {
            val offset = page * 20
            val pokemonListDto = pokeApi.getPokemonList(limit = 20, offset = offset)
            val pokemonList = pokemonListDto.results.map { it.toPokemon() }
            Result.success(pokemonList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPokemonDetail(name: String): Result<PokemonDetail> {
        return try {
            val pokemonDto = pokeApi.getPokemonDetail(name)
            val pokemonDetail = pokemonDto.toPokemonDetail()
            Result.success(pokemonDetail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFavoritePokemons(): Flow<List<Pokemon>> {
        return favoritePokemonDao.getFavoritePokemons().map { entities ->
            entities.map { it.toPokemon() }
        }
    }

    override suspend fun addFavorite(pokemon: PokemonDetail) {
        favoritePokemonDao.insertFavorite(pokemon.toFavoritePokemonEntity())
    }

    override suspend fun removeFavorite(pokemon: PokemonDetail) {
        favoritePokemonDao.deleteFavorite(pokemon.toFavoritePokemonEntity())
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return favoritePokemonDao.isFavorite(id)
    }

}
