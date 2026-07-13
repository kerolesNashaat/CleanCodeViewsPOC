package com.kiro.poc.data.dto


import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class CharactersResponseDto(
    @SerialName("info")
    val info: Info? = null,
    @SerialName("results")
    val results: List<CharacterDto?>? = null,
)