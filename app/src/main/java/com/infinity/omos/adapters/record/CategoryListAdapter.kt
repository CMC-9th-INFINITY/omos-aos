package com.infinity.omos.adapters.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.record.CategoryModel
import com.infinity.omos.databinding.ListItemCategoryBinding

class CategoryListAdapter :
    ListAdapter<CategoryModel, RecyclerView.ViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(
            ListItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = getItem(position)
        (holder as CategoryViewHolder).bind(category)
    }

    class CategoryViewHolder(
        private val binding: ListItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter = HorizontalRecordListAdapter()

        init {
            binding.layoutCategory.ivMove.setOnClickListener {
                // TODO: 상세 카테고리 이동
            }
        }

        fun bind(item: CategoryModel) {
            binding.apply {
                category = item
                layoutCategory.rvPreviewRecords.adapter = adapter
                adapter.submitList(item.records)
                executePendingBindings()
            }
        }
    }
}

private class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryModel>() {

    override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem.category == newItem.category
    }

    override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem == newItem
    }
}