package com.infinity.omos.adapters.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.record.DetailRecordModel
import com.infinity.omos.databinding.ListItemDetailRecordBinding

class DetailRecordPagingAdapter : PagingDataAdapter<DetailRecordModel, RecyclerView.ViewHolder>(DetailRecordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DetailRecordViewHolder(
            ListItemDetailRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val record = getItem(position)
        if (record != null) {
            (holder as DetailRecordViewHolder).bind(record)
        }
    }

    class DetailRecordViewHolder(
        private val binding: ListItemDetailRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetailRecordModel) {
            binding.apply {
                record = item
                executePendingBindings()
            }
        }
    }
}

private class DetailRecordDiffCallback : DiffUtil.ItemCallback<DetailRecordModel>() {
    override fun areItemsTheSame(oldItem: DetailRecordModel, newItem: DetailRecordModel): Boolean {
        return oldItem.recordId == newItem.recordId
    }

    override fun areContentsTheSame(
        oldItem: DetailRecordModel,
        newItem: DetailRecordModel
    ): Boolean {
        return oldItem == newItem
    }
}