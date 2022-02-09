package com.infinity.omos.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.MyRecord

object RecyclerViewBindingAdapter {
    @BindingAdapter("listData")
    @JvmStatic
    fun bindData(recyclerView: RecyclerView, records: List<MyRecord>?){
        val adapter = recyclerView.adapter as MyRecordListAdapter
        adapter.submitList(records)
    }
}