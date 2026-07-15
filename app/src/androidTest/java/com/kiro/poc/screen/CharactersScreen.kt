package com.kiro.poc.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.kiro.poc.R
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object CharactersScreen : KScreen<CharactersScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val recyclerView = KRecyclerView(
        builder = { withId(R.id.charactersRecyclerView) },
        itemTypeBuilder = { itemType(::CharacterItem) }
    )

    val loadingProgressBar = KProgressBar { withId(R.id.loadingProgressBar) }
    val errorText = KTextView { withId(R.id.errorTv) }
    val groupedViewFab = KView { withId(R.id.groupedViewFab) }
    val rowsViewFab = KView { withId(R.id.rowsViewFab) }

    class CharacterItem(matcher: Matcher<View>) : KRecyclerItem<CharacterItem>(matcher) {
        val name = KTextView(matcher) { withId(R.id.characterNameTv) }
        val status = KTextView(matcher) { withId(R.id.characterStatusTv) }
    }
}
