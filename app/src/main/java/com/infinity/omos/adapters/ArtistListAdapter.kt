package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.ArtistActivity
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.data.Artists
import com.infinity.omos.databinding.ListArtistItemBinding
import com.infinity.omos.databinding.ListLoadingItemBinding
import com.infinity.omos.etc.GlobalFunction

class ArtistListAdapter internal constructor(private val context: Context):
    ListAdapter<Artists, RecyclerView.ViewHolder>(
        ArtistDiffUtil
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var artist = ArrayList<Artists?>()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(v: View, position: Int, artist: Artists, tvGenres: String)
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
                val binding = ListArtistItemBinding.inflate(inflater,parent,false)
                ArtistViewHoler(binding)
            }

            else -> {
                val binding = ListLoadingItemBinding.inflate(inflater,parent,false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArtistViewHoler){
            val artist = artist[position]
            holder.bind(artist!!)
        }
    }

    inner class ArtistViewHoler(private val binding: ListArtistItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(artist: Artists) {
            binding.data = artist
            binding.tvGenres.text = setGenres(artist.genres)
            binding.tvArtistName.text = artist.artistName

            // keyword ?????? ??????
            var start = artist.artistName.lowercase().indexOf(MainActivity.keyword.lowercase())
            if (start != -1){
                GlobalFunction.changeTextColor(context, binding.tvArtistName, start, start + MainActivity.keyword.length, R.color.orange)
            }

            binding.executePendingBindings() //???????????? ???????????? ?????? ?????????

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    val intent = Intent(context, ArtistActivity::class.java)
                    intent.putExtra("artistId", artist.artistId)
                    intent.putExtra("artistName", artist.artistName)
                    intent.putExtra("artistImageUrl", artist.artistImageUrl)
                    intent.putExtra("artistGenres", binding.tvGenres.text.toString())
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class LoadingViewHolder(private val binding: ListLoadingItemBinding): RecyclerView.ViewHolder(binding.root){}

    private fun setGenres(genres: List<String>): String{
        var str = ""
        for (s in genres){
            str += "$s & "
        }

        if (str.length >= 3){
            str = str.substring(0, str.length - 3)
        } else{
            str = "No Genres"
        }

        return str
    }

    internal fun setRecord(ab: List<Artists>) {
        artist.addAll(ab)
        artist.add(null)
    }

    internal fun clearRecord(){
        artist.clear()
        notifyDataSetChanged()
    }

    internal fun deleteLoading(){
        artist.removeAt(artist.lastIndex) // ????????? ???????????? ????????????????????? ??????
    }

    override fun getItemViewType(position: Int): Int {
        return if (artist[position] == null){
            VIEW_TYPE_LOADING
        } else{
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return artist.size
    }

    companion object ArtistDiffUtil: DiffUtil.ItemCallback<Artists>(){
        override fun areItemsTheSame(oldItem: Artists, newItem: Artists): Boolean {
            //??? ??????????????? ????????? ?????? ???????????? ??????.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Artists, newItem: Artists): Boolean {
            return oldItem==newItem
        }
    }
}