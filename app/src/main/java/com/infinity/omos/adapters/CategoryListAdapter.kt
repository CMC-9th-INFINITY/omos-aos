package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MyRecordDetailActivity
import com.infinity.omos.data.DetailCategory
import com.infinity.omos.databinding.ListCategoryItemBinding

class CategoryListAdapter internal constructor(context: Context, category: List<DetailCategory>?):
    ListAdapter<DetailCategory, CategoryListAdapter.ViewHolder>(
        CategoryDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var record = category

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
        fun bind(record: DetailCategory) {
            binding.record = record
            binding.tvNick.text = "by. ${record.nickname}"

            var str = ""
            for (s in record.artists){
                str += s.artistName + ","
            }
            str.substring(0, str.length - 2)
            binding.tvArtist.text = str

            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    val intent = Intent(context, MyRecordDetailActivity::class.java)
                    intent.putExtra("title", record.musicTitle)
                    context.startActivity(intent)
                }
            }
        }
    }

    internal fun setRecords(record: List<DetailCategory>?) {
        this.record = record
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (record != null){
            record!!.size
        } else
            super.getItemCount()
    }

    companion object CategoryDiffUtil: DiffUtil.ItemCallback<DetailCategory>(){
        override fun areItemsTheSame(oldItem: DetailCategory, newItem: DetailCategory): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DetailCategory, newItem: DetailCategory): Boolean {
            return oldItem==newItem
        }
    }
}