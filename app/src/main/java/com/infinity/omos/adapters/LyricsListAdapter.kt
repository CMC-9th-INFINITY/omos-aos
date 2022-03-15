package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.DetailRecordActivity
import com.infinity.omos.data.Artists
import com.infinity.omos.data.Record
import com.infinity.omos.data.SumRecord
import com.infinity.omos.databinding.ListCategoryItemBinding
import com.infinity.omos.databinding.ListLyricsItemBinding
import com.infinity.omos.etc.GlobalFunction.Companion.setArtist
import com.infinity.omos.utils.GlobalApplication
import kotlinx.android.synthetic.main.list_lyrics_item.view.*

class LyricsListAdapter internal constructor(context: Context) :
    ListAdapter<String, LyricsListAdapter.ViewHolder>(
        LyricsDiffUtil
    ) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var lyrics = ArrayList<String>()
    private var interpret = ArrayList<String>()
    private var lengthList = ArrayList<Int>()
    private var textLength = 0
    private var currentPos = 0

    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var changeTextLengthListener: ChangeTextLengthListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    interface OnItemLongClickListener {
        fun onLongClick(v: View, position: Int)
    }

    interface ChangeTextLengthListener {
        fun changeLength(size: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun setChangeTextLengthListener(changeListener: ChangeTextLengthListener){
        this.changeTextLengthListener = changeListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ListLyricsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, LyricsTextWatcher())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lyrics = lyrics[position]

        if (position >= interpret.size) {
            interpret.add(position, "")
            lengthList.add(position, 0)
        }

        holder.lyricsTextWatcher.updatePosition(position)
        holder.itemView.et_interpret.setText(interpret[position])
        holder.bind(lyrics)
    }

    inner class ViewHolder(private val binding: ListLyricsItemBinding, val lyricsTextWatcher: LyricsTextWatcher)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyrics: String) {
            binding.lyrics = lyrics
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            binding.etInterpret.addTextChangedListener(lyricsTextWatcher)

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {

                }
            }
        }
    }

    internal fun setLyrics(lyrics: List<String>) {
        this.lyrics.clear()
        this.lyrics.addAll(lyrics)
        notifyDataSetChanged()
    }

    internal fun getContents(): String {
        var result = ""
        for (i in 0 until interpret.size) {
            if (interpret[i] != "") {
                result += "${lyrics[i]}\n${interpret[i]}\n"
            }
        }

        return result
    }

    override fun getItemCount(): Int {
        return lyrics.size
    }

    companion object LyricsDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class LyricsTextWatcher() : TextWatcher {
        private var position = 0

        fun updatePosition(position: Int){
            this.position = position
        }

        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val text = p0.toString()
            Log.d("jaemin", "$position / $text")
            interpret[position] = text
            lengthList[position] = text.length
            textLength = lengthList.sum() // EditText 총 글자 수
            changeTextLengthListener.changeLength(textLength)
        }
    }
}