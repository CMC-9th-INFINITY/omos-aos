package com.infinity.omos.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.user.Profile
import com.infinity.omos.data.SimpleRecord

object RecyclerViewBindingAdapter {
    @BindingAdapter("myRecordListData")
    @JvmStatic
    fun myRecordBindData(recyclerView: RecyclerView, records: List<SimpleRecord>?){
        val adapter = recyclerView.adapter as MyRecordListAdapter
        adapter.submitList(records)
    }

    @BindingAdapter("djRecordListData")
    @JvmStatic
    fun djRecordBindData(recyclerView: RecyclerView, records: List<SimpleRecord>?){
        val adapter = recyclerView.adapter as MyRecordListAdapter
        adapter.submitList(records)
    }

    @BindingAdapter("myDjListData")
    @JvmStatic
    fun myDjBindData(recyclerView: RecyclerView, dj: List<Profile>?){
        val adapter = recyclerView.adapter as MyDjListAdapter
        adapter.submitList(dj)
    }
}