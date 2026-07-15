package com.kiro.poc.presentation.characterRows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiro.poc.R
import com.kiro.poc.databinding.ItemCharacterCarouselBinding
import com.kiro.poc.domain.model.Character

/** Adapter for a single horizontal row of characters. Instances are reused across
 *  row rebinds (see [CharacterRowsAdapter.ViewHolder]) so only [submitList] is called
 *  on scroll, never a fresh adapter/RecyclerView attach. */
class CharacterCarouselAdapter(
    private val onCharacterClick: (Character) -> Unit
) : ListAdapter<Character, CharacterCarouselAdapter.ViewHolder>(DIFF) {

    init {
        setHasStableIds(true)
    }

    class ViewHolder(val binding: ItemCharacterCarouselBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemId(position: Int): Long = getItem(position).id.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCharacterCarouselBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_character_carousel,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        holder.binding.character = character
        holder.binding.root.setOnClickListener { onCharacterClick(character) }
        holder.binding.executePendingBindings()
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem == newItem
        }
    }
}
