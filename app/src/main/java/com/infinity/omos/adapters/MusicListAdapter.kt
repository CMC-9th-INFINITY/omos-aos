package com.infinity.omos.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MainActivity.Companion.keyword
import com.infinity.omos.R
import com.infinity.omos.data.Artists
import com.infinity.omos.data.Music
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.databinding.ListMusicItemBinding
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist

class MusicListAdapter internal constructor(private val context: Context):
    ListAdapter<Music, RecyclerView.ViewHolder>(
        MusicDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var music = ArrayList<Music?>()

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
                val binding = ListMusicItemBinding.inflate(inflater,parent,false)
                MusicViewHoler(binding)
            }

            else -> {
                val binding = ListLoadingItemBinding.inflate(inflater,parent,false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MusicViewHoler){
            val music = music[position]
            holder.bind(music!!)
        }
    }

    inner class MusicViewHoler(private val binding: ListMusicItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(music: Music) {
            binding.data = music
            binding.tvArtist.text = setArtist(music.artists)
            binding.tvMusicTitle.text = music.musicTitle

            // keyword 색상 변경
            var start = music.musicTitle.lowercase().indexOf(keyword.lowercase())
            if (start != -1){
                GlobalFunction.changeTextColor(context, binding.tvMusicTitle, start, start + keyword.length, R.color.orange)
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

    internal fun setRecord(ab: List<Music>) {
        music.addAll(ab)
        music.add(null)
    }

    internal fun clearRecord(){
        music.clear()
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

    companion object MusicDiffUtil: DiffUtil.ItemCallback<Music>(){
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem==newItem
        }
    }
}