package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.DjActivity
import com.infinity.omos.data.DetailCategory
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.ListDetailCategoryItemBinding
import kotlinx.android.synthetic.main.list_detail_category_item.view.*

class DetailCategoryListAdapter internal constructor(context: Context, category: List<DetailCategory>?):
    ListAdapter<DetailCategory, DetailCategoryListAdapter.ViewHolder>(
        DetailCategoryDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var category = category

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ListDetailCategoryItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = category!![position]
        holder.bind(category)
    }

    inner class ViewHolder(private val binding: ListDetailCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: DetailCategory){
            binding.data = category

            var str = ""
            for (s in category.artists){
                str += s.artistName + ","
            }
            str.substring(0, str.length - 2)
            binding.tvArtist.text = str

            binding.executePendingBindings()

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.btn_dj.setOnClickListener {
                    val intent = Intent(context, DjActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    internal fun updateCategory(category: List<DetailCategory>?) {
        this.category = category
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (category != null){
            category!!.size
        } else
            super.getItemCount()
    }

    companion object DetailCategoryDiffUtil: DiffUtil.ItemCallback<DetailCategory>(){
        override fun areItemsTheSame(oldItem: DetailCategory, newItem: DetailCategory): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DetailCategory, newItem: DetailCategory): Boolean {
            return oldItem==newItem
        }
    }
}