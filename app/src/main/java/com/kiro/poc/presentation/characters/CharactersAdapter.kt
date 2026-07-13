package com.kiro.poc.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kiro.poc.R
import com.kiro.poc.databinding.CharacterItemBinding
import com.kiro.poc.domain.model.Character

class CharactersAdapter(
    private val onCharacterClick: (Character) -> Unit
) : PagingDataAdapter<Character, CharactersAdapter.VH>(DIFF) {

    class VH(val binding: CharacterItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val binding: CharacterItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.character_item,
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val character = getItem(position)
        if (character != null) {
            holder.binding.character = character
            holder.binding.root.setOnClickListener { onCharacterClick(character) }
            holder.binding.executePendingBindings()
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(
                oldItem: Character,
                newItem: Character
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Character,
                newItem: Character
            ): Boolean = oldItem == newItem
        }
    }
}
