package com.example.mypokedex.data.di

import android.content.Context
import androidx.room.Room
import com.example.mypokedex.data.local.FavoritePokemonDao
import com.example.mypokedex.data.local.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoritePokemonDao(database: PokemonDatabase): FavoritePokemonDao {
        return database.favoritePokemonDao()
    }

}
