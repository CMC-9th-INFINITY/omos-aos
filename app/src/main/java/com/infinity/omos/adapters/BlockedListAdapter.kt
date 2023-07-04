package com.infinity.omos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.user.profile.Profile
import com.infinity.omos.databinding.ListBlockedAccountBinding

class BlockedListAdapter : ListAdapter<Profile, RecyclerView.ViewHolder>(BlockedDiffCallback()){

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(dj: Profile)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BlockedViewHolder(
            ListBlockedAccountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dj = getItem(position)
        (holder as BlockedViewHolder).bind(dj)
    }

    inner class BlockedViewHolder(
        private val binding: ListBlockedAccountBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener {
                binding.dj?.let { dj ->
                    itemClickListener.onClick(dj)
                }
            }
        }

        fun bind(item: Profile) {
            binding.apply {
                dj = item
                executePendingBindings()
            }
        }
    }

}

private class BlockedDiffCallback : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem == newItem
    }
}