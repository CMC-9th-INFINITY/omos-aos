package com.infinity.omos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infinity.omos.ui.dj.DjActivity
import com.infinity.omos.R
import com.infinity.omos.data.Profile
import com.infinity.omos.databinding.ListTodayDjItemBinding

class TodayDjListAdapter internal constructor(context: Context):
    ListAdapter<Profile, TodayDjListAdapter.ViewHolder>(
        TodayDjListAdapter
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context

    private var myDj = ArrayList<Profile>()

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(v: View, position: Int, toUserId: Int)
    }

    interface OnItemLongClickListener{
        fun onLongClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ListTodayDjItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dj = myDj[position]
        holder.bind(dj)
    }

    inner class ViewHolder(private val binding: ListTodayDjItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dj: Profile) {
            binding.dj = dj
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            Glide.with(binding.imgAlbumCover.context)
                .load(dj.profileUrl)
                .error(R.drawable.ic_profile)
                .fallback(R.drawable.ic_profile)
                .into(binding.imgAlbumCover)

            binding.imgAlbumCover.borderColor = ContextCompat.getColor(context, R.color.dark)

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    val intent = Intent(context, DjActivity::class.java)
                    intent.putExtra("toUserId", dj.userId)
                    context.startActivity(intent)
                }
            }
        }
    }

    internal fun setDj(dj: List<Profile>){
        myDj.clear()
        myDj.addAll(dj)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return myDj.size
    }

    companion object TodayDjDiffUtil: DiffUtil.ItemCallback<Profile>(){
        override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem==newItem
        }
    }
}