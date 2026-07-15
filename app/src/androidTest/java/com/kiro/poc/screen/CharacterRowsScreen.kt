package com.kiro.poc.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.kiro.poc.R
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object CharacterRowsScreen : KScreen<CharacterRowsScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val recyclerView = KRecyclerView(
        builder = { withId(R.id.characterRowsRecyclerView) },
        itemTypeBuilder = { itemType(::RowItem) }
    )

    val loadingProgressBar = KProgressBar { withId(R.id.loadingProgressBar) }
    val errorText = KTextView { withId(R.id.errorTv) }

    // item_character_carousel.xml has no view ids to key a nested KRecyclerItem off of,
    // so carousel character content is matched by top-level KTextView(text) instead.
    fun carouselCharacter(name: String) = KTextView { withText(name) }

    class RowItem(matcher: Matcher<View>) : KRecyclerItem<RowItem>(matcher) {
        val title = KTextView(matcher) { withId(R.id.rowTitleTv) }
    }
}
