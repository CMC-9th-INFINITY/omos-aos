package com.infinity.omos.adapters.dj

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.infinity.omos.data.user.profile.ProfileModel
import com.infinity.omos.databinding.ListItemRecommendedDjBinding

class RecommendedDjListAdapter :  ListAdapter<ProfileModel, RecyclerView.ViewHolder>(RecommendedDjDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecommendedDjViewHolder(
            ListItemRecommendedDjBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RecommendedDjViewHolder).bind(item)
    }

    class RecommendedDjViewHolder(
        private val binding: ListItemRecommendedDjBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                // TODO: 아이템 클릭 시
            }
        }

        fun bind(item: ProfileModel) {
            binding.apply {
                profile = item

                Glide.with(ivProfile.context)
                    .load(item.profileUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivProfile)

                executePendingBindings()
            }
        }
    }
}

private class RecommendedDjDiffCallback : DiffUtil.ItemCallback<ProfileModel>() {
    override fun areItemsTheSame(oldItem: ProfileModel, newItem: ProfileModel): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: ProfileModel, newItem: ProfileModel): Boolean {
        return oldItem == newItem
    }
}