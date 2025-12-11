package com.example.mypokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OtherSpritesDto(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtworkDto?
)
