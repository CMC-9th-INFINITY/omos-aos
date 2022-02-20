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
import com.infinity.omos.RecordDetailActivity
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.ListMyrecordItemBinding

class MyRecordListAdapter internal constructor(context: Context):
    ListAdapter<MyRecord, MyRecordListAdapter.ViewHolder>(
        MyRecordDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context

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
        val binding = ListMyrecordItemBinding.inflate(inflater,parent,false)

        // 앨범커버 반 가릴 수 있도록 커스텀
        // 일부 아이템 적용 안되는 문제 해결 (onPreDraw)
        binding.constraint.viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                var imgWidth = binding.imgAlbumCover.width
                var layoutWidth = binding.constraint.width

                val params = binding.constraint.layoutParams
                params.apply {
                    width = layoutWidth - imgWidth / 2
                }
                binding.constraint.apply {
                    layoutParams = params
                }

                binding.constraint.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = getItem(position)
        holder.bind(record)
    }

    inner class ViewHolder(private val binding: ListMyrecordItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(record: MyRecord) {
            binding.record = record
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    val intent = Intent(context, RecordDetailActivity::class.java)
                    intent.putExtra("title", record.title)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    companion object MyRecordDiffUtil: DiffUtil.ItemCallback<MyRecord>(){
        override fun areItemsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: MyRecord, newItem: MyRecord): Boolean {
            return oldItem==newItem
        }
    }
}