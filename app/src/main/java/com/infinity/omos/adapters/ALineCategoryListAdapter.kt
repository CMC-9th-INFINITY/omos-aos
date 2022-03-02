package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.DjActivity
import com.infinity.omos.R
import com.infinity.omos.data.DetailCategory
import com.infinity.omos.databinding.ListAlineCategoryItemBinding
import com.infinity.omos.databinding.ListDetailCategoryItemBinding
import kotlinx.android.synthetic.main.list_detail_category_item.view.*

class ALineCategoryListAdapter internal constructor(
    private val context: Context,
    private var category: List<DetailCategory>?
):
    ListAdapter<DetailCategory, ALineCategoryListAdapter.ViewHolder>(
        ALineCategoryDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ListAlineCategoryItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = category!![position]
        holder.bind(category)
    }

    inner class ViewHolder(private val binding: ListAlineCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: DetailCategory){
            binding.data = category
            binding.tvDj.text = "DJ ${category.nickname}"
            binding.tvContents.text = "\" ${category.recordContents} \""

            setArtist(binding, category)
            setDate(binding, category)
            setCategoryText(binding, category)

            binding.executePendingBindings()

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.tv_dj.setOnClickListener {
                    val intent = Intent(context, DjActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (category != null){
            category!!.size
        } else
            super.getItemCount()
    }

    private fun setArtist(binding: ListAlineCategoryItemBinding, record: DetailCategory){
        var str = ""
        for (s in record.music.artists){
            str += s.artistName + ", "
        }

        if (str.length >= 2){
            str = str.substring(0, str.length - 2)
            str += " - " + record.music.albumTitle
        } else{
            str = "ERROR"
        }

        binding.tvArtist.text = str
    }

    private fun setDate(binding: ListAlineCategoryItemBinding, record: DetailCategory){
        var listDate = record.createdDate.split("T")
        var date = listDate[0].replace("-", " ")
        binding.tvDate.text = date
    }

    private fun setCategoryText(binding: ListAlineCategoryItemBinding, record: DetailCategory){
        var ctg = ""
        when(record.category){
            "A_LINE" -> {
                ctg = context.resources.getString(R.string.a_line)
            }

            "STORY" -> {
                ctg = context.resources.getString(R.string.story)
            }

            "OST" -> {
                ctg = context.resources.getString(R.string.ost)
            }

            "LYRICS" -> {
                ctg = context.resources.getString(R.string.lyrics)
            }

            "FREE" -> {
                ctg = context.resources.getString(R.string.free)
            }
        }
        binding.tvCategory.text = ctg
    }

    companion object ALineCategoryDiffUtil: DiffUtil.ItemCallback<DetailCategory>(){
        override fun areItemsTheSame(oldItem: DetailCategory, newItem: DetailCategory): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DetailCategory, newItem: DetailCategory): Boolean {
            return oldItem==newItem
        }
    }
}