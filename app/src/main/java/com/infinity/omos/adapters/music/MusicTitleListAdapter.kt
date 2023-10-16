package com.infinity.omos.adapters.music

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.music.MusicTitleModel
import com.infinity.omos.databinding.ListItemKeywordBinding
import com.infinity.omos.utils.changeTextColor

class MusicTitleListAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<MusicTitleModel, RecyclerView.ViewHolder>(MusicTitleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MusicTitleViewHolder(
            onClick,
            ListItemKeywordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val musicTitle = getItem(position)
        (holder as MusicTitleViewHolder).bind(musicTitle)
    }

    class MusicTitleViewHolder(
        onClick: (String) -> Unit,
        private val binding: ListItemKeywordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(binding.title ?: "")
            }
        }

        fun bind(item: MusicTitleModel) {
            binding.apply {
                title = item.title
                executePendingBindings()
                // tvTitle.changeTextColor(item.keyword)
            }
        }
    }
}

private class MusicTitleDiffCallback : DiffUtil.ItemCallback<MusicTitleModel>() {

    override fun areItemsTheSame(oldItem: MusicTitleModel, newItem: MusicTitleModel): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: MusicTitleModel, newItem: MusicTitleModel): Boolean {
        return oldItem == newItem
    }
}