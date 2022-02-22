package com.infinity.omos.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.MyDj
import com.infinity.omos.data.MyRecord

object RecyclerViewBindingAdapter {
    @BindingAdapter("myRecordListData")
    @JvmStatic
    fun myRecordBindData(recyclerView: RecyclerView, records: List<MyRecord>?){
        val adapter = recyclerView.adapter as MyRecordListAdapter
        adapter.submitList(records)
    }

    @BindingAdapter("myDjListData")
    @JvmStatic
    fun myDjBindData(recyclerView: RecyclerView, dj: List<MyDj>?){
        val adapter = recyclerView.adapter as MyDjListAdapter
        adapter.submitList(dj)
    }
}