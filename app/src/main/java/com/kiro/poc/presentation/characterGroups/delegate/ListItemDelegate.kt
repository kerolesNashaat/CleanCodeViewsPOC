package com.kiro.poc.presentation.characterGroups.delegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiro.poc.presentation.characterGroups.CharacterListItem

/**
 * Isolates the create/bind logic for one [CharacterListItem] subtype so the adapter
 * itself never needs to know about individual view types.
 */
interface ListItemDelegate {
    fun isForItem(item: CharacterListItem): Boolean
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: CharacterListItem)
}
