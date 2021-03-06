package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.AlbumActivity
import com.infinity.omos.ArtistActivity
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.data.Album
import com.infinity.omos.databinding.ListAlbumItemBinding
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist

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
        fun onClick(v: View, position: Int, album: Album, tvArtist: String, tvDate: String)
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
                AlbumViewHolder(binding)
            }

            else -> {
                val binding = ListLoadingItemBinding.inflate(inflater,parent,false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AlbumViewHolder){
            val album = album[position]
            holder.bind(album!!)
        }
    }

    inner class AlbumViewHolder(private val binding: ListAlbumItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album) {
            binding.data = album
            binding.tvArtist.text = setArtist(album.artists)
            binding.tvDate.text = album.releaseDate.replace("-", " ")
            binding.tvAlbumTitle.text = album.albumTitle

            // keyword ?????? ??????
            var start = album.albumTitle.lowercase().indexOf(MainActivity.keyword.lowercase())
            if (start != -1 && context.javaClass != ArtistActivity::class.java){
                GlobalFunction.changeTextColor(context, binding.tvAlbumTitle, start, start + MainActivity.keyword.length, R.color.orange)
            }

            binding.executePendingBindings() //???????????? ???????????? ?????? ?????????

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    val intent = Intent(context, AlbumActivity::class.java)
                    intent.putExtra("albumTitle", album.albumTitle)
                    intent.putExtra("artists", binding.tvArtist.text.toString())
                    intent.putExtra("releaseDate", binding.tvDate.text.toString())
                    intent.putExtra("albumImageUrl", album.albumImageUrl)
                    intent.putExtra("albumId", album.albumId)
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class LoadingViewHolder(binding: ListLoadingItemBinding): RecyclerView.ViewHolder(binding.root)

    internal fun setRecord(ab: List<Album>) {
        album.addAll(ab)
        album.add(null)
    }

    internal fun clearRecord(){
        album.clear()
        notifyDataSetChanged()
    }

    internal fun deleteLoading(){
        album.removeAt(album.lastIndex) // ????????? ???????????? ????????????????????? ??????
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
            //??? ??????????????? ????????? ?????? ???????????? ??????.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem==newItem
        }
    }
}