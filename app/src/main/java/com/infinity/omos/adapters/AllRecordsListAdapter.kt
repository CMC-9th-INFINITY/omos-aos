package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.ui.record.CategoryRecordsActivity
import com.infinity.omos.R
import com.infinity.omos.data.FakeAllRecords
import com.infinity.omos.data.record.PreviewRecord
import com.infinity.omos.databinding.ListAllrecordsItemBinding
import com.infinity.omos.etc.GlobalFunction.Companion.changeTextColor
import kotlinx.android.synthetic.main.list_allrecords_item.view.*

class AllRecordsListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<AllRecordsListAdapter.AllRecordsViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var category = emptyList<FakeAllRecords>()

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
        fun bind(content: FakeAllRecords){
            binding.category = content

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.btn_category.setOnClickListener {
                    if (content.category!!.isNotEmpty()){
                        var intent = Intent(context, CategoryRecordsActivity::class.java)
                        intent.putExtra("category", content.title)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "레코드가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

/*            val mAdapter = HorizontalRecordListAdapter(context)
            mAdapter.setRecords(content.category)
            binding.rvCategory.apply{
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
            }*/

            // 레코드가 없을 때,
            if (content.category!!.isNotEmpty()){
                binding.lnNorecord.visibility = View.GONE
            } else{
                binding.lnNorecord.visibility = View.VISIBLE
                binding.tvNorecord.text = "${content.title}의\n첫번째 주인공이 되어봐요!"
                changeTextColor(context, binding.tvNorecord, 0, content.title.length, R.color.orange)
            }
        }
    }

    internal fun setCategory(category: List<FakeAllRecords>) {
        this.category = category
        notifyDataSetChanged()
    }

    internal fun updateCategory(record: List<PreviewRecord>?, pos: Int) {
        this.category[pos].category = record
        notifyDataSetChanged()
    }

    override fun getItemCount() = category.size
}