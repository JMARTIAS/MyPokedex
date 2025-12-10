package com.example.mypokedex.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(pokemon: FavoritePokemonEntity)

    @Delete
    suspend fun deleteFavorite(pokemon: FavoritePokemonEntity)

    @Query("SELECT * FROM favorite_pokemon")
    fun getFavoritePokemons(): Flow<List<FavoritePokemonEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_pokemon WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>

}
