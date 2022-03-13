package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.DetailRecordActivity
import com.infinity.omos.MyRecordDetailActivity
import com.infinity.omos.R
import com.infinity.omos.data.Record
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.databinding.ListMyrecordItemBinding
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist
import com.infinity.omos.etc.GlobalFunction.Companion.setCategoryText
import com.infinity.omos.etc.GlobalFunction.Companion.setDate

class MyRecordListAdapter internal constructor(context: Context):
    ListAdapter<Record, RecyclerView.ViewHolder>(
        MyRecordDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var record = ArrayList<Record?>()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{

        return when(viewType){
            VIEW_TYPE_ITEM -> {
                val binding = ListMyrecordItemBinding.inflate(inflater,parent,false)

                // 앨범커버 반 가릴 수 있도록 커스텀
                // 일부 아이템 적용 안되는 문제 해결 (onPreDraw)
                binding.constraint.viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener{
                    override fun onPreDraw(): Boolean {
                        var imgWidth = binding.imgAlbumCover.width
                        var layoutWidth = binding.constraint.width

                        val params = binding.constraint.layoutParams
                        params.apply {
                            width = layoutWidth - imgWidth * 9 / 16
                        }
                        binding.constraint.apply {
                            layoutParams = params
                        }

                        binding.constraint.viewTreeObserver.removeOnPreDrawListener(this)
                        return true
                    }
                })

                RecordViewHolder(binding)
            }

            else -> {
                val binding = ListLoadingItemBinding.inflate(inflater,parent,false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecordViewHolder){
            val record = record[position]
            holder.bind(record!!)
        }
    }

    inner class RecordViewHolder(private val binding: ListMyrecordItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(record: Record) {
            binding.data = record
            binding.tvArtist.text = setArtist(record.music.artists)
            binding.tvDate.text = setDate(record.createdDate)
            binding.tvCategory.text = setCategoryText(context, record.category)

            if (record.isPublic == true){
                binding.btnPublic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_public))
            } else {
                binding.btnPublic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_private))
            }

            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    /**
                     *  삭제 필요
                     */
//                    val intent = Intent(context, MyRecordDetailActivity::class.java)
//                    intent.putExtra("musicTitle", record.music.musicTitle)
//                    intent.putExtra("artists", binding.tvArtist.text.toString())
//                    intent.putExtra("albumTitle", record.music.albumTitle)
//                    intent.putExtra("albumImageUrl", record.music.albumImageUrl)
//                    intent.putExtra("recordTitle", record.recordTitle)
//                    intent.putExtra("recordContents", record.recordContents)
//                    intent.putExtra("date", binding.tvDate.text.toString())
//                    intent.putExtra("category", binding.tvCategory.text.toString())
//                    intent.putExtra("isPublic", record.isPublic)
//                    intent.putExtra("isLiked", record.isLiked)
//                    intent.putExtra("isScraped", record.isScraped)
//                    intent.putExtra("likeCnt", record.likeCnt)
//                    intent.putExtra("scrapCnt", record.scrapCnt)
//                    intent.putExtra("nickname", record.nickname)

                    val intent = Intent(context, DetailRecordActivity::class.java)
                    intent.putExtra("postId", record.recordId)
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class LoadingViewHolder(private val binding: ListLoadingItemBinding): RecyclerView.ViewHolder(binding.root){}

    internal fun setRecord(rec: List<Record>) {
        record.addAll(rec)
        notifyDataSetChanged()
    }

    internal fun addRecord(rec: List<Record>) {
        record.addAll(rec)
        record.add(null)
    }

    internal fun deleteLoading(){
        record.removeAt(record.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    override fun getItemViewType(position: Int): Int {
        return if (record[position] == null){
            VIEW_TYPE_LOADING
        } else{
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return record.size
    }

    companion object MyRecordDiffUtil: DiffUtil.ItemCallback<Record>(){
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem==newItem
        }
    }
}