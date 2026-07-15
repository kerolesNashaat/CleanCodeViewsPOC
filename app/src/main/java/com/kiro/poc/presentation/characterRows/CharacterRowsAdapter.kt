package com.kiro.poc.presentation.characterRows

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiro.poc.R
import com.kiro.poc.databinding.ItemCharacterRowBinding
import com.kiro.poc.domain.model.Character

/**
 * Vertical adapter of horizontally-scrolling rows. All nested RecyclerViews share one
 * [RecyclerView.RecycledViewPool] so item views recycle across rows instead of each row
 * inflating and discarding its own, and each row's [CharacterCarouselAdapter] is created
 * once per ViewHolder and only re-fed via [ListAdapter.submitList] on rebind.
 */
class CharacterRowsAdapter(
    private val onCharacterClick: (Character) -> Unit
) : ListAdapter<CharacterRow, CharacterRowsAdapter.ViewHolder>(DIFF) {

    init {
        setHasStableIds(true)
    }

    private val sharedRecycledViewPool = RecyclerView.RecycledViewPool()

    // Nested horizontal scroll offset per row id, so scrolling the parent list away
    // from a row and back doesn't reset that row's carousel to the start.
    private val nestedScrollStates = HashMap<String, Parcelable>()

    override fun getItemId(position: Int): Long = getItem(position).id.hashCode().toLong()

    inner class ViewHolder(val binding: ItemCharacterRowBinding) : RecyclerView.ViewHolder(binding.root) {
        private val carouselAdapter = CharacterCarouselAdapter(onCharacterClick)
        private val carouselLayoutManager = LinearLayoutManager(
            binding.root.context, LinearLayoutManager.HORIZONTAL, false
        ).apply {
            initialPrefetchItemCount = 4
        }

        private var boundRowId: String? = null

        init {
            binding.rowRecyclerView.apply {
                adapter = carouselAdapter
                layoutManager = carouselLayoutManager
                setRecycledViewPool(sharedRecycledViewPool)
                setHasFixedSize(true)
                itemAnimator = null
            }
        }

        fun bind(row: CharacterRow) {
            saveScrollState()
            boundRowId = row.id
            binding.title = row.title
            binding.executePendingBindings()
            carouselAdapter.submitList(row.characters) { restoreScrollState(row.id) }
        }

        fun saveScrollState() {
            val id = boundRowId ?: return
            carouselLayoutManager.onSaveInstanceState()?.let { nestedScrollStates[id] = it }
        }

        private fun restoreScrollState(rowId: String) {
            val state = nestedScrollStates[rowId]
            if (state != null) {
                carouselLayoutManager.onRestoreInstanceState(state)
            } else {
                carouselLayoutManager.scrollToPosition(0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCharacterRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_character_row,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.saveScrollState()
        super.onViewRecycled(holder)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CharacterRow>() {
            override fun areItemsTheSame(oldItem: CharacterRow, newItem: CharacterRow): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CharacterRow, newItem: CharacterRow): Boolean =
                oldItem == newItem
        }
    }
}
