package com.infinity.omos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.DetailCategory
import com.infinity.omos.databinding.ListAlineCategoryItemBinding

class ALineCategoryListAdapter internal constructor(context: Context, category: List<DetailCategory>?):
    ListAdapter<DetailCategory, ALineCategoryListAdapter.ViewHolder>(
        ALineCategoryDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var category = category

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ListAlineCategoryItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = category!![position]
        holder.bind(category)
    }

    inner class ViewHolder(private val binding: ListAlineCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: DetailCategory){
            binding.data = category
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return if (category != null){
            category!!.size
        } else
            super.getItemCount()
    }

    companion object ALineCategoryDiffUtil: DiffUtil.ItemCallback<DetailCategory>(){
        override fun areItemsTheSame(oldItem: DetailCategory, newItem: DetailCategory): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DetailCategory, newItem: DetailCategory): Boolean {
            return oldItem==newItem
        }
    }
}