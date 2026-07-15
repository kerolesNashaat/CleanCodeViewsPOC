package com.kiro.poc.fakes

import com.kiro.poc.domain.model.Character

object TestCharacters {

    val rick = Character(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        gender = "Male",
        origin = "Earth (C-137)",
        location = "Earth (C-137)",
        image = ""
    )

    val morty = Character(
        id = 2,
        name = "Morty Smith",
        status = "Alive",
        species = "Human",
        gender = "Male",
        origin = "Earth (C-137)",
        location = "Earth (Replacement Dimension)",
        image = ""
    )

    val summer = Character(
        id = 3,
        name = "Summer Smith",
        status = "Dead",
        species = "Human",
        gender = "Female",
        origin = "Earth (Replacement Dimension)",
        location = "Earth (Replacement Dimension)",
        image = ""
    )

    fun sampleList(): List<Character> = listOf(rick, morty, summer)
}
