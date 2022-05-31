package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.DetailRecordActivity
import com.infinity.omos.R
import com.infinity.omos.data.SimpleRecord
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.databinding.ListMyrecordItemBinding
import com.infinity.omos.etc.GlobalFunction.Companion.extractLetter
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist
import com.infinity.omos.etc.GlobalFunction.Companion.setCategoryText
import com.infinity.omos.etc.GlobalFunction.Companion.setDate
import com.infinity.omos.utils.GlobalApplication

class MyRecordListAdapter internal constructor(context: Context):
    ListAdapter<SimpleRecord, RecyclerView.ViewHolder>(
        MyRecordDiffUtil
    ), Filterable{

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var record = ArrayList<SimpleRecord>()
    private var recordAll = ArrayList<SimpleRecord>()
    private var recordFilter = ArrayList<SimpleRecord>()
    private var isDj = false

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    private lateinit var itemClickListener: OnItemClickListener
    private val userId = GlobalApplication.prefs.getInt("userId")

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
        fun bind(record: SimpleRecord) {
            binding.data = record
            binding.tvArtist.text = setArtist(record.music.artists)
            binding.tvDate.text = setDate(record.createdDate)
            binding.tvCategory.text = setCategoryText(context, record.category)

            if (record.isPublic == false){
                binding.btnPublic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_private))
            } else {
                binding.btnPublic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_public))
            }

            // DJ 레코드에서는 안보이게
            if (isDj){
                binding.btnPublic.visibility = View.GONE
            }

            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    val intent = Intent(context, DetailRecordActivity::class.java)
                    intent.putExtra("postId", record.recordId)
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class LoadingViewHolder(private val binding: ListLoadingItemBinding): RecyclerView.ViewHolder(binding.root){}

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                if (charSearch.isEmpty()) {
                    recordFilter = recordAll
                } else {
                    val resultList = ArrayList<SimpleRecord>()
                    for (row in recordAll) {
                        var recordTitle = extractLetter(row.recordTitle.lowercase())
                        var musicTitle = extractLetter(row.music.musicTitle.lowercase())
                        var search = extractLetter(charSearch.lowercase())

                        if(charSearch.length == 1){
                            when (charSearch) {
                                recordTitle.substring(0,1) -> {
                                    resultList.add(row)
                                }
                                row.recordTitle.substring(0,1) -> {
                                    resultList.add(row)
                                }
                                musicTitle.substring(0,1) -> {
                                    resultList.add(row)
                                }
                                row.music.musicTitle.substring(0,1) -> {
                                    resultList.add(row)
                                }
                            }
                        } else{
                            if (recordTitle.contains(search)){
                                resultList.add(row)
                            } else if (musicTitle.contains(search)){
                                resultList.add(row)
                            }
                        }
                    }
                    recordFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = recordFilter
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                recordFilter = p1?.values as ArrayList<SimpleRecord>
                filterRecord(recordFilter)
            }
        }
    }

    internal fun setRecord(rec: List<SimpleRecord>) {
        record.clear()
        recordAll.clear()
        recordAll.addAll(rec)
        record.addAll(recordAll)
        notifyDataSetChanged()
    }

    internal fun filterRecord(rec: List<SimpleRecord>) {
        record.clear()
        record.addAll(rec)
        notifyDataSetChanged()
    }

    internal fun setDjRecord(rec: List<SimpleRecord>) {
        isDj = true
        record.clear()
        recordAll.clear()

        /** 현재 MY레코드 불러오기 API를 사용하므로,
         *  비공개글은 보여주면 안됨
         *  추후에 API 변경 시 수정 필요
         */

        for (i in rec){
            if (i.isPublic == true){
                record.add(i)
                recordAll.add(i)
            }
        }

        notifyDataSetChanged()
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

    companion object MyRecordDiffUtil: DiffUtil.ItemCallback<SimpleRecord>(){
        override fun areItemsTheSame(oldItem: SimpleRecord, newItem: SimpleRecord): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: SimpleRecord, newItem: SimpleRecord): Boolean {
            return oldItem==newItem
        }
    }
}