package com.infinity.omos.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.ListCategoryItemBinding

class CategoryListAdapter internal constructor(context: Context, myRecord: List<MyRecord>):
    ListAdapter<MyRecord, CategoryListAdapter.ViewHolder>(
        CategoryDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var record = myRecord

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
        val binding = ListCategoryItemBinding.inflate(inflater,parent,false)

        binding.imgAlbumCover.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                var imgWidth = binding.imgAlbumCover.width
                var layoutWidth = binding.constraint.width

                val params = binding.constraint.layoutParams
                params.apply {
                    width = layoutWidth - imgWidth/2
                }
                binding.constraint.apply {
                    layoutParams = params
                }

                binding.imgAlbumCover.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
//
//        var params = binding.constraint.layoutParams
//        var metrics = context.resources.displayMetrics
//        Log.d("TEST", metrics.widthPixels.toString())
//        Log.d("TEST", binding.imgAlbumCover.width.toString())
//        params.width = metrics.widthPixels - (binding.imgAlbumCover.width / 2)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = record[position]
        holder.bind(record)
    }

    inner class ViewHolder(private val binding: ListCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(record: MyRecord) {
            binding.record = record
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    Toast.makeText(context, "클릭", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    internal fun setRecords(record: List<MyRecord>) {
        this.record = record
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return record.size
    }

    companion object CategoryDiffUtil: DiffUtil.ItemCallback<MyRecord>(){
        override fun areItemsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
            return oldItem==newItem
        }
    }
}