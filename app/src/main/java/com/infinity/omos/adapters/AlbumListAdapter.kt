package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MainActivity
import com.infinity.omos.MyRecordDetailActivity
import com.infinity.omos.R
import com.infinity.omos.data.Album
import com.infinity.omos.data.Artists
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.ListAlbumItemBinding
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.databinding.ListMyrecordItemBinding
import com.infinity.omos.etc.GlobalFunction

class AlbumListAdapter internal constructor(private val context: Context):
    ListAdapter<Album, RecyclerView.ViewHolder>(
        AlbumDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var album = ArrayList<Album?>()

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
                val binding = ListAlbumItemBinding.inflate(inflater,parent,false)
                AlbumViewHoler(binding)
            }

            else -> {
                val binding = ListLoadingItemBinding.inflate(inflater,parent,false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AlbumViewHoler){
            val album = album[position]
            holder.bind(album!!)
        }
    }

    inner class AlbumViewHoler(private val binding: ListAlbumItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album) {
            binding.data = album
            binding.tvArtist.text = setArtist(album.artists)
            binding.tvDate.text = album.releaseDate.replace("-", " ")
            binding.tvAlbumTitle.text = album.albumTitle

            // keyword 색상 변경
            var start = album.albumTitle.lowercase().indexOf(MainActivity.keyword.lowercase())
            if (start != -1){
                GlobalFunction.changeTextColor(context, binding.tvAlbumTitle, start, start + MainActivity.keyword.length, R.color.orange)
            }

            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {

                }
            }
        }
    }

    inner class LoadingViewHolder(private val binding: ListLoadingItemBinding): RecyclerView.ViewHolder(binding.root){}

    private fun setArtist(artists: List<Artists>): String{
        var str = ""
        for (s in artists){
            str += s.artistName + " & "
        }

        if (str.length >= 3){
            str = str.substring(0, str.length - 3)
        } else{
            str = "ERROR"
        }

        return str
    }

    internal fun setRecord(ab: List<Album>) {
        album.addAll(ab)
        album.add(null)
    }

    internal fun clearRecord(){
        album.clear()
    }

    internal fun deleteLoading(){
        album.removeAt(album.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    override fun getItemViewType(position: Int): Int {
        return if (album[position] == null){
            VIEW_TYPE_LOADING
        } else{
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return album.size
    }

    companion object AlbumDiffUtil: DiffUtil.ItemCallback<Album>(){
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem==newItem
        }
    }
}