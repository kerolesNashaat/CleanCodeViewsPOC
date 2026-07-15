package com.kiro.poc.presentation.characterGroups

import com.kiro.poc.domain.model.Character

sealed class CharacterListItem {
    data class Header(val title: String) : CharacterListItem()
    data class CharacterRow(val character: Character) : CharacterListItem()
}

fun List<Character>.toCharacterListItems(): List<CharacterListItem> {
    return groupBy { it.status.ifBlank { "Unknown" } }
        .toSortedMap(compareBy { STATUS_ORDER.indexOf(it).let { i -> if (i == -1) STATUS_ORDER.size else i } })
        .flatMap { (status, characters) ->
            listOf(CharacterListItem.Header(status)) + characters.map { CharacterListItem.CharacterRow(it) }
        }
}

private val STATUS_ORDER = listOf("Alive", "Dead", "Unknown")
