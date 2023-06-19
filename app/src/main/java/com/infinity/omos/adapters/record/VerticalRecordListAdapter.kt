package com.infinity.omos.adapters.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.record.VerticalPreviewRecordModel
import com.infinity.omos.databinding.ListItemVerticalRecordBinding

class VerticalRecordListAdapter :
    ListAdapter<VerticalPreviewRecordModel, RecyclerView.ViewHolder>(VerticalRecordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VerticalRecordViewHolder(
            ListItemVerticalRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val record = getItem(position)
        (holder as VerticalRecordViewHolder).bind(record)
    }

    class VerticalRecordViewHolder(
        private val binding: ListItemVerticalRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                // TODO: 아이템 클릭 시
            }
        }

        fun bind(item: VerticalPreviewRecordModel) {
            binding.apply {
                record = item
                executePendingBindings()
            }
        }
    }
}

private class VerticalRecordDiffCallback : DiffUtil.ItemCallback<VerticalPreviewRecordModel>() {

    override fun areItemsTheSame(
        oldItem: VerticalPreviewRecordModel,
        newItem: VerticalPreviewRecordModel
    ): Boolean {
        return oldItem.recordId == newItem.recordId
    }

    override fun areContentsTheSame(
        oldItem: VerticalPreviewRecordModel,
        newItem: VerticalPreviewRecordModel
    ): Boolean {
        return oldItem == newItem
    }
}