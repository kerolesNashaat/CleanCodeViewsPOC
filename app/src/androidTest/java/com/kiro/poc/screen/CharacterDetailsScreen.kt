package com.kiro.poc.screen

import com.kaspersky.kaspresso.screens.KScreen
import com.kiro.poc.R
import io.github.kakaocup.kakao.text.KTextView

object CharacterDetailsScreen : KScreen<CharacterDetailsScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val nameText = KTextView { withId(R.id.nameTv) }
    val statusSpeciesText = KTextView { withId(R.id.statusSpeciesTv) }
    val locationText = KTextView { withId(R.id.locationTv) }
}
