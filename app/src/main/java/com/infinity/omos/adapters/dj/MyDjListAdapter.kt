package com.infinity.omos.adapters.dj

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.user.ProfileModel
import com.infinity.omos.databinding.ListItemMyDjBinding

class MyDjListAdapter : ListAdapter<ProfileModel, RecyclerView.ViewHolder>(MyDjDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyDjViewHolder(
            ListItemMyDjBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as MyDjViewHolder).bind(item)
    }

    class MyDjViewHolder(
        private val binding: ListItemMyDjBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                // TODO: 테두리 생성
            }
        }

        fun bind(item: ProfileModel) {
            binding.apply {
                profile = item
                executePendingBindings()
            }
        }
    }
}

private class MyDjDiffCallback : DiffUtil.ItemCallback<ProfileModel>() {
    override fun areItemsTheSame(oldItem: ProfileModel, newItem: ProfileModel): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: ProfileModel, newItem: ProfileModel): Boolean {
        return oldItem == newItem
    }
}