package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.DjActivity
import com.infinity.omos.R
import com.infinity.omos.data.LikeScrap
import com.infinity.omos.data.Record
import com.infinity.omos.databinding.ListDetailCategoryItemBinding
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist
import com.infinity.omos.etc.GlobalFunction.Companion.setCategoryText
import com.infinity.omos.etc.GlobalFunction.Companion.setDate
import kotlinx.android.synthetic.main.list_detail_category_item.view.*

class DetailCategoryListAdapter internal constructor(
    private val context: Context
):
    ListAdapter<Record, RecyclerView.ViewHolder>(
        DetailCategoryDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var category = ArrayList<Record?>()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    private lateinit var itemClickListener: OnItemClickListener

    private var heartStarList = ArrayList<LikeScrap>()

    private var saveHeartList = ArrayList<Int>()
    private var deleteHeartList = ArrayList<Int>()

    private var saveScrapList = ArrayList<Int>()
    private var deleteScrapList = ArrayList<Int>()

    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }

    interface OnItemLongClickListener{
        fun onLongClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

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
            holder.bind(category!!, position)
        }
    }

    inner class CategoryViewHolder(private val binding: ListDetailCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: Record, num: Int){
            binding.data = category
            binding.tvDj.text = "DJ ${category.nickname}"
            binding.tvArtist.text = setArtist(category.music.artists) + " - " + category.music.albumTitle
            binding.tvCategory.text = setCategoryText(context, category.category)
            binding.tvDate.text = setDate(category.createdDate)

            if (category.category == "A_LINE"){
                binding.tvAlineContents.visibility = View.VISIBLE
                binding.tvRecordContents.visibility = View.GONE
                binding.tvAlineContents.text = "\"${category.recordContents}\""
            } else {
                binding.tvAlineContents.visibility = View.GONE
                binding.tvRecordContents.visibility = View.VISIBLE
                binding.tvRecordContents.text = category.recordContents
            }

            if (num >= heartStarList.size){
                heartStarList.add(num, LikeScrap(category.isLiked, category.isScraped, category.likeCnt, category.scrapCnt))
            }
            setHeartStar(binding, heartStarList[num])

            binding.executePendingBindings()

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.tv_dj.setOnClickListener {
                    val intent = Intent(context, DjActivity::class.java)
                    context.startActivity(intent)
                }

                itemView.btn_heart.setOnClickListener {
                    if (heartStarList[num].isLiked){
                        itemView.img_heart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unchecked_heart))
                        itemView.tv_heart_cnt.setTextColor(ContextCompat.getColor(context, R.color.gray_03))
                        itemView.tv_heart_cnt.text = (Integer.parseInt(itemView.tv_heart_cnt.text.toString()) - 1).toString()
                        heartStarList[num].isLiked = false

                        // 상태 변화 확인
                        if (category.isLiked){
                            deleteHeartList.add(category.recordId)
                        } else{
                            saveHeartList.remove(category.recordId)
                        }
                    } else{
                        itemView.img_heart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_checked_heart))
                        itemView.tv_heart_cnt.setTextColor(ContextCompat.getColor(context, R.color.orange))
                        itemView.tv_heart_cnt.text = (Integer.parseInt(itemView.tv_heart_cnt.text.toString()) + 1).toString()
                        heartStarList[num].isLiked = true

                        if (category.isLiked){
                            deleteHeartList.remove(category.recordId)
                        } else{
                            saveHeartList.add(category.recordId)
                        }
                    }
                }

                itemView.btn_star.setOnClickListener {
                    if (heartStarList[num].isScraped){
                        itemView.img_star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unchecked_star))
                        itemView.tv_star_cnt.setTextColor(ContextCompat.getColor(context, R.color.gray_03))
                        itemView.tv_star_cnt.text = (Integer.parseInt(itemView.tv_star_cnt.text.toString()) - 1).toString()
                        heartStarList[num].isScraped = false

                        // 상태 변화 확인
                        if (category.isScraped){
                            deleteScrapList.add(category.recordId)
                        } else{
                            saveScrapList.remove(category.recordId)
                        }
                    } else{
                        itemView.img_star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_checked_star))
                        itemView.tv_star_cnt.setTextColor(ContextCompat.getColor(context, R.color.orange))
                        itemView.tv_star_cnt.text = (Integer.parseInt(itemView.tv_star_cnt.text.toString()) + 1).toString()
                        heartStarList[num].isScraped = true

                        if (category.isLiked){
                            deleteScrapList.remove(category.recordId)
                        } else{
                            saveScrapList.add(category.recordId)
                        }
                    }
                }
            }
        }
    }

    inner class LoadingViewHolder(binding: ListLoadingItemBinding): RecyclerView.ViewHolder(binding.root)

    internal fun getSaveHeart(): List<Int>{
        return saveHeartList
    }

    internal fun getDeleteHeart(): List<Int>{
        return deleteHeartList
    }

    internal fun getSaveScrap(): List<Int>{
        return saveScrapList
    }

    internal fun getDeleteScrap(): List<Int>{
        return deleteScrapList
    }

    internal fun changeState(position: Int){
        category[position]?.isLiked = category[position]?.isLiked != true
        notifyItemChanged(position)
    }

    internal fun setCategory(category: List<Record>){
        this.category.addAll(category)
        notifyDataSetChanged()
    }

    internal fun addCategory(category: List<Record>){
        this.category.addAll(category)
        this.category.add(null)
    }

    internal fun deleteLoading(){
        category.removeAt(category.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    private fun setHeartStar(binding: ListDetailCategoryItemBinding, data: LikeScrap){
        if (data.isLiked){
            binding.imgHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_checked_heart))
            binding.tvHeartCnt.setTextColor(ContextCompat.getColor(context, R.color.orange))
        } else{
            binding.imgHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unchecked_heart))
            binding.tvHeartCnt.setTextColor(ContextCompat.getColor(context, R.color.gray_03))
        }

        if (data.isScraped){
            binding.imgStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_checked_star))
            binding.tvStarCnt.setTextColor(ContextCompat.getColor(context, R.color.orange))
        } else{
            binding.imgStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unchecked_star))
            binding.tvStarCnt.setTextColor(ContextCompat.getColor(context, R.color.gray_03))
        }
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

    companion object DetailCategoryDiffUtil: DiffUtil.ItemCallback<Record>(){
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem==newItem
        }
    }
}