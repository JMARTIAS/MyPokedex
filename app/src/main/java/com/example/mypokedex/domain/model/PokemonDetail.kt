package com.example.mypokedex.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int,
    val weight: Int,
    val abilities: List<Ability>,
    val stats: List<Stat>,
    val types: List<String>
)
