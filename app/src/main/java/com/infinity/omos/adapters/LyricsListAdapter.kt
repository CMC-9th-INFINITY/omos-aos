package com.infinity.omos.adapters

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.databinding.ListLyricsItemBinding
import kotlinx.android.synthetic.main.list_lyrics_item.view.*

class LyricsListAdapter internal constructor(context: Context) :
    ListAdapter<String, RecyclerView.ViewHolder>(
        LyricsDiffUtil
    ) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private var lyrics = ArrayList<String>()
    private var interpret = ArrayList<String>()
    private var lengthList = ArrayList<Int>()
    private var textLength = 0
    private var isWrite = false

    private val VIEW_TYPE_WRITE = 0
    private val VIEW_TYPE_READ = 1

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            VIEW_TYPE_WRITE -> {
                val binding = ListLyricsItemBinding.inflate(inflater, parent, false)
                WriteViewHolder(binding, LyricsTextWatcher())
            }

            else -> {
                val binding = ListLyricsItemBinding.inflate(inflater, parent, false)
                ReadViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WriteViewHolder){
            val lyrics = lyrics[position]
            if (position >= interpret.size) {
                interpret.add(position, "")
            }
            if (position >= lengthList.size){
                lengthList.add(position, interpret[position].length)
            }
            holder.lyricsTextWatcher.updatePosition(position)
            holder.itemView.et_interpret.setText(interpret[position])

            // Multi-line EditText with Done(Next) action button - Stack Overflow
            holder.itemView.et_interpret.imeOptions = EditorInfo.IME_ACTION_NEXT
            holder.itemView.et_interpret.setRawInputType(InputType.TYPE_CLASS_TEXT)

            holder.bind(lyrics, interpret[position])
        } else if (holder is ReadViewHolder){
            val lyrics = lyrics[position]
            holder.bind(lyrics, interpret[position])
        }
    }

    inner class WriteViewHolder(private val binding: ListLyricsItemBinding, val lyricsTextWatcher: LyricsTextWatcher)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyrics: String, interpret: String) {
            binding.lyrics = lyrics
            binding.interpret = interpret
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            // 글자수 세기
            binding.etInterpret.addTextChangedListener(lyricsTextWatcher)

            // 엔터키 막기
            binding.etInterpret.setOnKeyListener { view, i, keyEvent ->
                when(i){
                    KeyEvent.KEYCODE_ENTER -> {
                        true
                    }

                    else -> false
                }
            }

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {

                }
            }
        }
    }

    inner class ReadViewHolder(private val binding: ListLyricsItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyrics: String, interpret: String) {
            binding.lyrics = lyrics
            binding.interpret = interpret
            binding.etInterpret.isEnabled = false
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {

                }
            }
        }
    }

    internal fun setLyrics(lyrics: List<String>, state: Boolean) {
        this.isWrite = state
        this.lyrics.clear()
        this.lyrics.addAll(lyrics)
        notifyDataSetChanged()
    }

    internal fun setInterpret(interpret: List<String>){
        this.interpret.clear()
        this.interpret.addAll(interpret)
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

    override fun getItemViewType(position: Int): Int {
        return if (isWrite){
            VIEW_TYPE_WRITE
        } else{
            VIEW_TYPE_READ
        }
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
            interpret[position] = text
            lengthList[position] = text.length
            textLength = lengthList.sum() // EditText 총 글자 수
            changeTextLengthListener.changeLength(textLength)
        }
    }
}