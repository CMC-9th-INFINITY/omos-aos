package com.infinity.omos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.Profile
import com.infinity.omos.databinding.ListFollowerItemBinding

class FollowerListAdapter : ListAdapter<Profile, RecyclerView.ViewHolder>(FollowerDiffCallback()) {
    private lateinit var buttonClickListener: OnItemClickListener
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(dj: Profile)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    fun setButtonClickListener(onItemClickListener: OnItemClickListener){
        this.buttonClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FollowerViewHolder(
            ListFollowerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dj = getItem(position)
        (holder as FollowerViewHolder).bind(dj)
    }

    inner class FollowerViewHolder(
        private val binding: ListFollowerItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setItemClickListener {
                binding.dj?.let { dj ->
                    itemClickListener.onClick(dj)
                }
            }

            binding.setButtonClickListener {
                binding.dj?.let { dj ->
                    buttonClickListener.onClick(dj)
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

private class FollowerDiffCallback : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem == newItem
    }
}