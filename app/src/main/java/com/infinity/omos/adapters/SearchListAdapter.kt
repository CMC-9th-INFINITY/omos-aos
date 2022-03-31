package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.data.AllRecords
import com.infinity.omos.data.Music
import com.infinity.omos.data.SumRecord
import com.infinity.omos.databinding.ListSearchItemBinding
import com.infinity.omos.etc.GlobalFunction
import kotlinx.android.synthetic.main.list_allrecords_item.view.*
import kotlinx.android.synthetic.main.list_search_item.view.*

class SearchListAdapter internal constructor(private val context: Context)
    : RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var nameList = ArrayList<Music>()

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(v: View, position: Int, title: String)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ListSearchItemBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(nameList[position])
    }

    inner class SearchViewHolder(val binding: ListSearchItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(music: Music) {
            binding.tvMusicTitle.text = music.musicTitle

            // keyword 색상 변경
            var start = music.musicTitle.lowercase().indexOf(MainActivity.keyword.lowercase())
            if (start != -1){
                GlobalFunction.changeTextColor(context, binding.tvMusicTitle, start, start + MainActivity.keyword.length, R.color.orange)
            }

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.btn_music.setOnClickListener {
                    itemClickListener.onClick(itemView, pos, music.musicTitle)
                }
            }
        }
    }

    internal fun setMusicTitle(name: List<Music>) {
        nameList.clear()
        nameList.addAll(name)
        notifyDataSetChanged()
    }

    internal fun clearSearch() {
        nameList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = nameList.size
}