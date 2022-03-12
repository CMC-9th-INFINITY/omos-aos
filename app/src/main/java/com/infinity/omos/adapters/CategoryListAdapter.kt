package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MyRecordDetailActivity
import com.infinity.omos.UserRecordDetailActivity
import com.infinity.omos.data.Record
import com.infinity.omos.databinding.ListCategoryItemBinding
import com.infinity.omos.etc.GlobalFunction.Companion.categoryEngToKr
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist
import com.infinity.omos.etc.GlobalFunction.Companion.setDate
import com.infinity.omos.utils.GlobalApplication

class CategoryListAdapter internal constructor(context: Context, category: List<Record>?):
    ListAdapter<Record, CategoryListAdapter.ViewHolder>(
        CategoryDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var record = category

    private val userId = GlobalApplication.prefs.getInt("userId")

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }

    interface OnItemLongClickListener{
        fun onLongClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ListCategoryItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = record!![position]
        holder.bind(record)
    }

    inner class ViewHolder(private val binding: ListCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(record: Record) {
            binding.record = record
            binding.tvNick.text = "by. ${record.nickname}"
            binding.tvArtist.text = setArtist(record.music.artists) + " - " + record.music.albumTitle

            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    val intent = Intent(context, UserRecordDetailActivity::class.java)
                    intent.putExtra("postId", record.recordId)
                    context.startActivity(intent)
                }
            }
        }
    }

    internal fun setRecords(record: List<Record>?) {
        this.record = record
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (record != null){
            record!!.size
        } else
            super.getItemCount()
    }

    companion object CategoryDiffUtil: DiffUtil.ItemCallback<Record>(){
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem==newItem
        }
    }
}