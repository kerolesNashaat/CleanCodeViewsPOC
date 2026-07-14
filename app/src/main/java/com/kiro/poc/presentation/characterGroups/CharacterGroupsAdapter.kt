package com.kiro.poc.presentation.characterGroups

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiro.poc.domain.model.Character
import com.kiro.poc.presentation.characterGroups.delegate.CharacterHeaderDelegate
import com.kiro.poc.presentation.characterGroups.delegate.CharacterRowDelegate
import com.kiro.poc.presentation.characterGroups.delegate.ListItemDelegate

class CharacterGroupsAdapter(
    onCharacterClick: (Character) -> Unit
) : ListAdapter<CharacterListItem, RecyclerView.ViewHolder>(DIFF) {

    private val delegates: List<ListItemDelegate> = listOf(
        CharacterHeaderDelegate(),
        CharacterRowDelegate(onCharacterClick)
    )

    private fun delegateFor(item: CharacterListItem): ListItemDelegate =
        delegates.first { it.isForItem(item) }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return delegates.indexOfFirst { it.isForItem(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegates[viewType].onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        delegateFor(item).onBindViewHolder(holder, item)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CharacterListItem>() {
            override fun areItemsTheSame(
                oldItem: CharacterListItem,
                newItem: CharacterListItem
            ): Boolean = when {
                oldItem is CharacterListItem.Header && newItem is CharacterListItem.Header ->
                    oldItem.title == newItem.title
                oldItem is CharacterListItem.CharacterRow && newItem is CharacterListItem.CharacterRow ->
                    oldItem.character.id == newItem.character.id
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: CharacterListItem,
                newItem: CharacterListItem
            ): Boolean = oldItem == newItem
        }
    }
}
