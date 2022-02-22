package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.AllRecords
import com.infinity.omos.data.MyDj
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.ListMydjItemBinding

class MyDjListAdapter internal constructor(context: Context):
    ListAdapter<MyDj, MyDjListAdapter.ViewHolder>(
        MyDjListAdapter
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(v: View, position: Int, records: List<MyRecord>?)
    }

    interface OnItemLongClickListener{
        fun onLongClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ListMydjItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dj = getItem(position)
        holder.bind(dj)
    }

    inner class ViewHolder(private val binding: ListMydjItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dj: MyDj) {
            binding.dj = dj
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    Toast.makeText(context, "클릭", Toast.LENGTH_SHORT).show()
                    itemClickListener.onClick(itemView, pos, dj.records)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    companion object MyDjDiffUtil: DiffUtil.ItemCallback<MyDj>(){
        override fun areItemsTheSame(oldItem: MyDj, newItem: MyDj): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: MyDj, newItem: MyDj): Boolean {
            return oldItem==newItem
        }
    }
}