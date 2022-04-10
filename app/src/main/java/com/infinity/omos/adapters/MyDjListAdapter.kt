package com.infinity.omos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infinity.omos.R
import com.infinity.omos.data.Profile
import com.infinity.omos.databinding.ListMydjItemBinding

class MyDjListAdapter internal constructor(context: Context):
    ListAdapter<Profile, MyDjListAdapter.ViewHolder>(
        MyDjListAdapter
    ){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context

    private var myDj = ArrayList<Profile>()

    private var isChecked = ArrayList<Boolean>()
    private var prevChecked = 0

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
        var binding = ListMydjItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dj = myDj[position]
        holder.bind(dj, position)
    }

    inner class ViewHolder(private val binding: ListMydjItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dj: Profile, num: Int) {
            binding.dj = dj
            binding.executePendingBindings() //데이터가 수정되면 즉각 바인딩

            if (num >= isChecked.size){
                isChecked.add(num, false)

                Glide.with(binding.imgAlbumCover.context)
                    .load(dj.profileUrl)
                    .error(R.drawable.ic_profile)
                    .fallback(R.drawable.ic_profile)
                    .into(binding.imgAlbumCover)
            }

            if (isChecked[num]){
                binding.imgAlbumCover.borderColor = ContextCompat.getColor(context, R.color.orange)
            } else{
                binding.imgAlbumCover.borderColor = ContextCompat.getColor(context, R.color.dark)
            }

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    // ViewHolder 재사용 방지
                    if (!isChecked[num]){
                        isChecked[prevChecked] = false
                        isChecked[num] = true
                        prevChecked = num
                        notifyDataSetChanged() // 이미지 재적용
                        itemClickListener.onClick(itemView, pos, dj.userId)
                    } else{
                        isChecked[num] = false
                        notifyDataSetChanged()
                        itemClickListener.onClick(itemView, -1, dj.userId)
                    }
                }
            }
        }
    }

    internal fun setDj(dj: List<Profile>){
        // 초기화
        isChecked = ArrayList()
        prevChecked = 0

        myDj.clear()
        myDj.addAll(dj)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return myDj.size
    }

    companion object MyDjDiffUtil: DiffUtil.ItemCallback<Profile>(){
        override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            //각 아이템들의 고유한 값을 비교해야 한다.
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem==newItem
        }
    }
}