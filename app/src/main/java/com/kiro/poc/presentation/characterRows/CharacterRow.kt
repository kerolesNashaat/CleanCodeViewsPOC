package com.kiro.poc.presentation.characterRows

import com.kiro.poc.domain.model.Character

data class CharacterRow(
    val id: String,
    val title: String,
    val characters: List<Character>
)

fun List<Character>.toCharacterRows(): List<CharacterRow> {
    return groupBy { it.status.ifBlank { "Unknown" } }
        .toSortedMap(compareBy { status -> STATUS_ORDER.indexOf(status).let { i -> if (i == -1) STATUS_ORDER.size else i } })
        .map { (status, characters) -> CharacterRow(id = status, title = status, characters = characters) }
}

private val STATUS_ORDER = listOf("Alive", "Dead", "Unknown")
