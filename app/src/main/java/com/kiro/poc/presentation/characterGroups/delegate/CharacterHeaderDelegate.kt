package com.kiro.poc.presentation.characterGroups.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kiro.poc.R
import com.kiro.poc.databinding.ItemCharacterGroupHeaderBinding
import com.kiro.poc.presentation.characterGroups.CharacterListItem

class CharacterHeaderDelegate : ListItemDelegate {

    class ViewHolder(val binding: ItemCharacterGroupHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun isForItem(item: CharacterListItem): Boolean = item is CharacterListItem.Header

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding: ItemCharacterGroupHeaderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_character_group_header,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: CharacterListItem) {
        item as CharacterListItem.Header
        (holder as ViewHolder).binding.title = item.title
        holder.binding.executePendingBindings()
    }
}
