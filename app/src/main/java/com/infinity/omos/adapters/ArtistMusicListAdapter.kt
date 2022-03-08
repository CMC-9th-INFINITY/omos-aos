package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MainActivity.Companion.keyword
import com.infinity.omos.R
import com.infinity.omos.SelectCategoryActivity
import com.infinity.omos.data.ArtistMusic
import com.infinity.omos.data.Artists
import com.infinity.omos.data.Music
import com.infinity.omos.databinding.ListArtistMusicItemBinding
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.databinding.ListMusicItemBinding
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist

class ArtistMusicListAdapter internal constructor(private val context: Context):
    ListAdapter<ArtistMusic, ArtistMusicListAdapter.MusicViewHolder>(
        MusicDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var music = ArrayList<ArtistMusic?>()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder{
        val binding = ListArtistMusicItemBinding.inflate(inflater,parent,false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = music[position]
        holder.bind(music!!)
    }

    inner class MusicViewHolder(private val binding: ListArtistMusicItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(music: ArtistMusic) {
            binding.data = music
            binding.tvArtist.text = setArtist(music.artistName)
            binding.tvMusicTitle.text = music.musicTitle

            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    val intent = Intent(context, SelectCategoryActivity::class.java)
                    intent.putExtra("musicId", music.musicId)
                    intent.putExtra("musicTitle", music.musicTitle)
                    intent.putExtra("artists", binding.tvArtist.text.toString())
                    intent.putExtra("albumImageUrl", music.albumImageUrl)
                    context.startActivity(intent)
                }
            }
        }
    }

    fun setArtist(artists: List<String>): String{
        var str = ""
        for (s in artists){
            str += "$s & "
        }

        if (str.length >= 3){
            str = str.substring(0, str.length - 3)
        } else{
            str = "-"
        }

        return str
    }

    internal fun setRecord(ab: List<ArtistMusic>) {
        music.addAll(ab)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return music.size
    }

    companion object MusicDiffUtil: DiffUtil.ItemCallback<ArtistMusic>(){
        override fun areItemsTheSame(oldItem: ArtistMusic, newItem: ArtistMusic): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ArtistMusic, newItem: ArtistMusic): Boolean {
            return oldItem==newItem
        }
    }
}