package com.infinity.omos.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.MyDj
import com.infinity.omos.data.Profile
import com.infinity.omos.data.Record
import com.infinity.omos.data.SaveRecord

object RecyclerViewBindingAdapter {
    @BindingAdapter("myRecordListData")
    @JvmStatic
    fun myRecordBindData(recyclerView: RecyclerView, records: List<Record>?){
        val adapter = recyclerView.adapter as MyRecordListAdapter
        adapter.submitList(records)
    }

    @BindingAdapter("djRecordListData")
    @JvmStatic
    fun djRecordBindData(recyclerView: RecyclerView, records: List<Record>?){
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