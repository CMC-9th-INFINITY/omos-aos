package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.DjActivity
import com.infinity.omos.R
import com.infinity.omos.data.DetailCategory
import com.infinity.omos.databinding.ListAlineCategoryItemBinding
import com.infinity.omos.databinding.ListDetailCategoryItemBinding
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist
import kotlinx.android.synthetic.main.list_detail_category_item.view.*

class DetailCategoryListAdapter internal constructor(
    private val context: Context
):
    ListAdapter<DetailCategory, RecyclerView.ViewHolder>(
        DetailCategoryDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var category = ArrayList<DetailCategory?>()
    private var stateHeart = false
    private var stateStar = false

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_ITEM -> {
                var binding = ListDetailCategoryItemBinding.inflate(inflater, parent, false)
                CategoryViewHolder(binding)
            }

            else -> {
                val binding = ListLoadingItemBinding.inflate(inflater,parent,false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder){
            val category = category[position]
            holder.bind(category!!)
        }
    }

    inner class CategoryViewHolder(private val binding: ListDetailCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: DetailCategory){
            binding.data = category
            binding.tvDj.text = "DJ ${category.nickname}"
            binding.tvArtist.text = setArtist(category.music.artists) + " - " + category.music.albumTitle

            setDate(binding, category)
            setCategoryText(binding, category)
            setHeartStar(binding, category)

            binding.executePendingBindings()

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.tv_dj.setOnClickListener {
                    val intent = Intent(context, DjActivity::class.java)
                    context.startActivity(intent)
                }

                itemView.btn_heart.setOnClickListener {
                    stateHeart = if (stateHeart){
                        itemView.img_heart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unchecked_heart))
                        itemView.tv_heart_cnt.setTextColor(ContextCompat.getColor(context, R.color.gray_03))
                        binding.tvHeartCnt.text = (Integer.parseInt(binding.tvHeartCnt.text.toString()) - 1).toString()
                        false
                    } else{
                        itemView.img_heart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_checked_heart))
                        itemView.tv_heart_cnt.setTextColor(ContextCompat.getColor(context, R.color.orange))
                        binding.tvHeartCnt.text = (Integer.parseInt(binding.tvHeartCnt.text.toString()) + 1).toString()
                        true
                    }
                }

                itemView.btn_star.setOnClickListener {
                    stateStar = if (stateStar){
                        itemView.img_star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unchecked_star))
                        itemView.tv_star_cnt.setTextColor(ContextCompat.getColor(context, R.color.gray_03))
                        binding.tvStarCnt.text = (Integer.parseInt(binding.tvStarCnt.text.toString()) - 1).toString()
                        false
                    } else{
                        itemView.img_star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_checked_star))
                        itemView.tv_star_cnt.setTextColor(ContextCompat.getColor(context, R.color.orange))
                        binding.tvStarCnt.text = (Integer.parseInt(binding.tvStarCnt.text.toString()) + 1).toString()
                        true
                    }
                }
            }
        }
    }

    inner class LoadingViewHolder(binding: ListLoadingItemBinding): RecyclerView.ViewHolder(binding.root)

    internal fun addCategory(category: List<DetailCategory>){
        this.category.addAll(category)
        this.category.add(null)
    }

    internal fun deleteLoading(){
        category.removeAt(category.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    private fun setHeartStar(binding: ListDetailCategoryItemBinding, record: DetailCategory){
        if (record.isLiked){
            binding.imgHeart.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange))
            binding.tvHeartCnt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange))
            stateHeart = true
        }

        if (record.isScraped){
            binding.imgStar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange))
            binding.tvStarCnt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange))
            stateStar = true
        }
    }

    private fun setDate(binding: ListDetailCategoryItemBinding, record: DetailCategory){
        var listDate = record.createdDate.split("T")
        var date = listDate[0].replace("-", " ")
        binding.tvDate.text = date
    }

    private fun setCategoryText(binding: ListDetailCategoryItemBinding, record: DetailCategory){
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

    override fun getItemCount(): Int {
        return if (category != null){
            category!!.size
        } else
            super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return if (category[position] == null){
            VIEW_TYPE_LOADING
        } else{
            VIEW_TYPE_ITEM
        }
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