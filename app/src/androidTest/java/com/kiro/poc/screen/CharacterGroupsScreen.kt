package com.kiro.poc.screen

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.kaspersky.kaspresso.screens.KScreen
import com.kiro.poc.R
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object CharacterGroupsScreen : KScreen<CharacterGroupsScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val recyclerView = KRecyclerView(
        builder = { withId(R.id.characterGroupsRecyclerView) },
        itemTypeBuilder = {
            itemType(::HeaderItem)
            itemType(::CharacterRowItem)
        }
    )

    val loadingProgressBar = KProgressBar { withId(R.id.loadingProgressBar) }
    val errorText = KTextView { withId(R.id.errorTv) }

    class HeaderItem(matcher: Matcher<View>) : KRecyclerItem<HeaderItem>(matcher) {
        // item_character_group_header.xml's root IS the groupHeaderTv TextView itself
        // (not wrapped in a container), so it can't be found as a descendant of itself -
        // assert directly on the item's own root view instead of a nested KTextView.
        fun hasTitle(text: String) = matches { withText(text) }
    }

    class CharacterRowItem(matcher: Matcher<View>) : KRecyclerItem<CharacterRowItem>(matcher) {
        val name = KTextView(matcher) { withId(R.id.characterNameTv) }
    }
}
