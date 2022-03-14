package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.DjActivity
import com.infinity.omos.R
import com.infinity.omos.data.*
import com.infinity.omos.databinding.ListMydjItemBinding

class TodayDjListAdapter internal constructor(context: Context):
    ListAdapter<Profile, TodayDjListAdapter.ViewHolder>(
        TodayDjListAdapter
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context

    private var myDj = ArrayList<Profile>()

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
        val dj = myDj[position]
        holder.bind(dj)
    }

    inner class ViewHolder(private val binding: ListMydjItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dj: Profile) {
            binding.dj = dj
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    val intent = Intent(context, DjActivity::class.java)
                    intent.putExtra("toUserId", dj.userId)
                    context.startActivity(intent)
                }
            }
        }
    }

    internal fun setDj(dj: List<Profile>){
        myDj.clear()
        myDj.addAll(dj)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return myDj.size
    }

    companion object TodayDjDiffUtil: DiffUtil.ItemCallback<Profile>(){
        override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem==newItem
        }
    }
}