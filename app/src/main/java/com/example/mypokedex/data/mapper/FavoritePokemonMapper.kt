package com.example.mypokedex.data.mapper

import com.example.mypokedex.data.local.FavoritePokemonEntity
import com.example.mypokedex.domain.model.Pokemon
import com.example.mypokedex.domain.model.PokemonDetail

fun FavoritePokemonEntity.toPokemon(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        imageUrl = imageUrl
    )
}

fun PokemonDetail.toFavoritePokemonEntity(): FavoritePokemonEntity {
    return FavoritePokemonEntity(
        id = id,
        name = name,
        imageUrl = imageUrl
    )
}
