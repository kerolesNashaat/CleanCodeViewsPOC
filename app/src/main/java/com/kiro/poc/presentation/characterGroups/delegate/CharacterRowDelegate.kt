package com.kiro.poc.presentation.characterGroups.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kiro.poc.R
import com.kiro.poc.databinding.CharacterItemBinding
import com.kiro.poc.domain.model.Character
import com.kiro.poc.presentation.characterGroups.CharacterListItem

class CharacterRowDelegate(
    private val onCharacterClick: (Character) -> Unit
) : ListItemDelegate {

    class ViewHolder(val binding: CharacterItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun isForItem(item: CharacterListItem): Boolean = item is CharacterListItem.CharacterRow

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding: CharacterItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.character_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: CharacterListItem) {
        item as CharacterListItem.CharacterRow
        val character = item.character
        (holder as ViewHolder).binding.character = character
        holder.binding.root.setOnClickListener { onCharacterClick(character) }
        holder.binding.executePendingBindings()
    }
}
