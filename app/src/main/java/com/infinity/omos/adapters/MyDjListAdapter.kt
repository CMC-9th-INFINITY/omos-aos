package com.infinity.omos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.MyDj
import com.infinity.omos.data.Profile
import com.infinity.omos.data.Record
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.databinding.ListMydjItemBinding

class MyDjListAdapter internal constructor(context: Context):
    ListAdapter<Profile, MyDjListAdapter.ViewHolder>(
        MyDjListAdapter
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(v: View, position: Int, toUserId: Int)
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
        fun bind(dj: Profile) {
            binding.dj = dj
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    itemClickListener.onClick(itemView, pos, dj.userId)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    companion object MyDjDiffUtil: DiffUtil.ItemCallback<Profile>(){
        override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem==newItem
        }
    }
}