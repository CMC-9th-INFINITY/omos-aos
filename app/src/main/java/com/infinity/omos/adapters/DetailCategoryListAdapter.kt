package com.infinity.omos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.ListOnelineCategoryItemBinding

class DetailCategoryListAdapter internal constructor(context: Context, myRecord: List<MyRecord>?):
    ListAdapter<MyRecord, DetailCategoryListAdapter.ViewHolder>(
        DetailCategoryDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var record = myRecord

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ListOnelineCategoryItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = record!![position]
        holder.bind(record)
    }

    inner class ViewHolder(private val binding: ListOnelineCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(record: MyRecord){
            binding.data = record
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return if (record != null){
            record!!.size
        } else
            super.getItemCount()
    }

    companion object DetailCategoryDiffUtil: DiffUtil.ItemCallback<MyRecord>(){
        override fun areItemsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
            return oldItem==newItem
        }
    }
}