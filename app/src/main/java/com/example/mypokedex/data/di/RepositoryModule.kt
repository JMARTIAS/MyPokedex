package com.example.mypokedex.data.di

import com.example.mypokedex.data.repository.LanguageRepositoryImpl
import com.example.mypokedex.data.repository.PokemonRepositoryImpl
import com.example.mypokedex.data.repository.UserRepositoryImpl
import com.example.mypokedex.domain.repository.LanguageRepository
import com.example.mypokedex.domain.repository.PokemonRepository
import com.example.mypokedex.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPokemonRepository(pokemonRepositoryImpl: PokemonRepositoryImpl): PokemonRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindLanguageRepository(languageRepositoryImpl: LanguageRepositoryImpl): LanguageRepository
}
