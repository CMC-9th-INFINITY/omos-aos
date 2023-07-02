package com.infinity.omos.adapters.dj

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.user.ProfileModel
import com.infinity.omos.databinding.ListItemMyDjBinding

class MyDjListAdapter(
    private val onItemClick: (Int, Boolean) -> Unit
) : ListAdapter<ProfileModel, RecyclerView.ViewHolder>(MyDjDiffCallback()) {

    val selectedPosition = SelectedPosition(RecyclerView.NO_POSITION, RecyclerView.NO_POSITION)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyDjViewHolder(
            ListItemMyDjBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            selectedPosition,
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as MyDjViewHolder).bind(item, position)
    }

    class MyDjViewHolder(
        private val binding: ListItemMyDjBinding,
        private val selectedPosition: SelectedPosition,
        private val onItemClick: (Int, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            var isSelected = true
            itemView.setOnClickListener {
                selectedPosition.apply {
                    prevPosition = newPosition
                    newPosition = absoluteAdapterPosition

                    if (prevPosition == newPosition) {
                        newPosition = RecyclerView.NO_POSITION
                        isSelected = false
                    }
                }
                onItemClick(binding.profile?.userId ?: -1, isSelected)
            }
        }

        fun bind(
            item: ProfileModel,
            position: Int
        ) {
            binding.apply {
                profile = item
                binding.ivProfile.isSelected =
                    position == selectedPosition.newPosition
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

data class SelectedPosition(
    var prevPosition: Int,
    var newPosition: Int
)