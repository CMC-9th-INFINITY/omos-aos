package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.AllRecordDetailActivity
import com.infinity.omos.data.AllRecords
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.ListAllrecordsItemBinding
import kotlinx.android.synthetic.main.list_allrecords_item.view.*
import kotlinx.android.synthetic.main.list_category_item.view.constraint
import kotlinx.android.synthetic.main.list_category_item.view.img_album_cover

class AllRecordsListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<AllRecordsListAdapter.AllRecordsViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var category = emptyList<AllRecords>()

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: AllRecordsListAdapter.OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRecordsViewHolder {
        val binding = ListAllrecordsItemBinding.inflate(inflater, parent, false)
        return AllRecordsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllRecordsViewHolder, position: Int) {
        holder.bind(category[position])
    }

    inner class AllRecordsViewHolder(val binding: ListAllrecordsItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(content: AllRecords){
            binding.category = content

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.btn_category.setOnClickListener {
                    var intent = Intent(context, AllRecordDetailActivity::class.java)
                    intent.putExtra("category", content.title)
                    context.startActivity(intent)
                }
            }

            val mAdapter = CategoryListAdapter(context, content.myRecord)
            mAdapter.setRecords(content.myRecord)
            binding.rvCategory.apply{
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            }

            // 레코드가 없을 때,
            if (content.myRecord == null){
                binding.lnNorecord.visibility = View.VISIBLE
            } else{
                binding.lnNorecord.visibility = View.GONE
            }
        }
    }

    internal fun setCategory(category: List<AllRecords>) {
        this.category = category
        notifyDataSetChanged()
    }

    internal fun updateCategory(record: List<MyRecord>?, pos: Int) {
        this.category[pos].myRecord = record
        notifyDataSetChanged()
    }

    override fun getItemCount() = category.size
}