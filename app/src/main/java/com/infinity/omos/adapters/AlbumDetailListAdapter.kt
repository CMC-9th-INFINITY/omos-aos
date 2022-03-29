package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MainActivity.Companion.isWrite
import com.infinity.omos.MusicRecordActivity
import com.infinity.omos.SelectCategoryActivity
import com.infinity.omos.data.Music
import com.infinity.omos.databinding.ListAlbumDetailItemBinding
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist

class AlbumDetailListAdapter internal constructor(private val context: Context):
    ListAdapter<Music, RecyclerView.ViewHolder>(
        AlbumDetailMusicDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var music = ArrayList<Music?>()
    private var albumImageUrl: String = ""

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
                val binding = ListAlbumDetailItemBinding.inflate(inflater,parent,false)
                MusicViewHolder(binding)
            }

            else -> {
                val binding = ListLoadingItemBinding.inflate(inflater,parent,false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MusicViewHolder){
            val music = music[position]
            holder.bind(music!!)
        }
    }

    inner class MusicViewHolder(private val binding: ListAlbumDetailItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(music: Music) {
            binding.data = music
            binding.tvArtist.text = setArtist(music.artists)
            binding.tvMusicTitle.text = music.musicTitle
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            // 글쓰기 아이콘 유무
            if (isWrite){
                binding.btnWrite.visibility = View.VISIBLE
            } else{
                binding.btnWrite.visibility = View.GONE
            }

            val pos = adapterPosition
            binding.tvCount.text = String.format("%02d", pos+1)
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    if (isWrite){
                        val intent = Intent(context, SelectCategoryActivity::class.java)
                        intent.putExtra("musicId", music.musicId)
                        intent.putExtra("musicTitle", music.musicTitle)
                        intent.putExtra("artists", binding.tvArtist.text.toString())
                        intent.putExtra("albumImageUrl", albumImageUrl)
                        context.startActivity(intent)
                    } else {
                        val intent = Intent(context, MusicRecordActivity::class.java)
                        intent.putExtra("musicId", music.musicId)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    inner class LoadingViewHolder(binding: ListLoadingItemBinding): RecyclerView.ViewHolder(binding.root)

    internal fun setImageUrl(url: String?) {
        if (url != null){
            albumImageUrl = url
        }
    }

    internal fun setRecord(ab: List<Music>) {
        music.addAll(ab)
        music.add(null)
    }

    internal fun deleteLoading(){
        music.removeAt(music.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    override fun getItemViewType(position: Int): Int {
        return if (music[position] == null){
            VIEW_TYPE_LOADING
        } else{
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return music.size
    }

    companion object AlbumDetailMusicDiffUtil: DiffUtil.ItemCallback<Music>(){
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem==newItem
        }
    }
}