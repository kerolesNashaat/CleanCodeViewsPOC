package com.kiro.poc.data.mapper

import com.kiro.poc.data.dto.CharacterDto
import com.kiro.poc.domain.model.Character

fun CharacterDto.toDomain(): Character = Character(
    id = id ?: 0,
    name = name.orEmpty(),
    status = status.orEmpty(),
    species = species.orEmpty(),
    gender = gender.orEmpty(),
    origin = origin?.name.orEmpty(),
    location = location?.name.orEmpty(),
    image = image.orEmpty()
)

fun List<CharacterDto?>.toDomain(): List<Character> = mapNotNull { it?.toDomain() }
