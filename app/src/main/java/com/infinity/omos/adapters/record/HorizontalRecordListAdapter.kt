package com.infinity.omos.adapters.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.record.HorizontalPreviewRecordModel
import com.infinity.omos.databinding.ListItemHorizontalRecordBinding

class HorizontalRecordListAdapter : ListAdapter<HorizontalPreviewRecordModel, RecyclerView.ViewHolder>(HorizontalRecordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HorizontalRecordViewHolder(
            ListItemHorizontalRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val record = getItem(position)
        (holder as HorizontalRecordViewHolder).bind(record)
    }

    class HorizontalRecordViewHolder(
        private val binding: ListItemHorizontalRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                // TODO: 아이템 클릭 시
            }
        }

        fun bind(item: HorizontalPreviewRecordModel) {
            binding.apply {
                record = item
                executePendingBindings()
            }
        }
    }
}

private class HorizontalRecordDiffCallback : DiffUtil.ItemCallback<HorizontalPreviewRecordModel>() {

    override fun areItemsTheSame(oldItem: HorizontalPreviewRecordModel, newItem: HorizontalPreviewRecordModel): Boolean {
        return oldItem.recordId == newItem.recordId
    }

    override fun areContentsTheSame(oldItem: HorizontalPreviewRecordModel, newItem: HorizontalPreviewRecordModel): Boolean {
        return oldItem == newItem
    }
}