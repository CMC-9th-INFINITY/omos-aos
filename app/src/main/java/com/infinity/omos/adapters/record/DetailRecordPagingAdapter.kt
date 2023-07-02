package com.infinity.omos.adapters.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.record.DetailRecordModel
import com.infinity.omos.databinding.ListItemDetailRecordBinding
import com.infinity.omos.utils.DataStoreManager
import javax.inject.Inject

class DetailRecordPagingAdapter @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : PagingDataAdapter<DetailRecordModel, RecyclerView.ViewHolder>(DetailRecordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DetailRecordViewHolder(
            dataStoreManager.getUserId(),
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
        private val id: Int,
        private val binding: ListItemDetailRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetailRecordModel) {
            binding.apply {
                record = item
                userId = id
                executePendingBindings()
            }
        }
    }
}

private class DetailRecordDiffCallback : DiffUtil.ItemCallback<DetailRecordModel>() {
    override fun areItemsTheSame(oldItem: DetailRecordModel, newItem: DetailRecordModel): Boolean {
        return false
    }

    override fun areContentsTheSame(
        oldItem: DetailRecordModel,
        newItem: DetailRecordModel
    ): Boolean {
        return oldItem == newItem
    }
}