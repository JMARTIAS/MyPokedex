package com.example.mypokedex.data.remote.dto

data class PokemonDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilityHolderDto>,
    val stats: List<StatHolderDto>,
    val types: List<TypeHolderDto>,
    val sprites: SpritesDto
)
