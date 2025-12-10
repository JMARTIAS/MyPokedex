package com.example.mypokedex.data.mapper

import com.example.mypokedex.data.remote.dto.PokemonDto
import com.example.mypokedex.data.remote.dto.PokemonListItemDto
import com.example.mypokedex.domain.model.Ability
import com.example.mypokedex.domain.model.Pokemon
import com.example.mypokedex.domain.model.PokemonDetail
import com.example.mypokedex.domain.model.Stat

fun PokemonListItemDto.toPokemon(): Pokemon {
    val id = url.split("/").last { it.isNotEmpty() }.toInt()
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    return Pokemon(
        id = id,
        name = name,
        imageUrl = imageUrl
    )
}

fun PokemonDto.toPokemonDetail(): PokemonDetail {
    return PokemonDetail(
        id = id,
        name = name,
        imageUrl = sprites.other.officialArtwork.frontDefault,
        height = height,
        weight = weight,
        abilities = abilities.map { Ability(it.ability.name) },
        stats = stats.map { Stat(it.stat.name, it.base_stat) },
        types = types.map { it.type.name }
    )
}
