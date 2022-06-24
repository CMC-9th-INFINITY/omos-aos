package com.infinity.omos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.data.Profile
import com.infinity.omos.databinding.ListBlockedAccountBinding
import com.infinity.omos.utils.CustomDialog

class BlockedListAdapter internal constructor(private val context: Context) :
    ListAdapter<Profile, RecyclerView.ViewHolder>(BlockedDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BlockedViewHolder(
            ListBlockedAccountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dj = getItem(position)
        (holder as BlockedViewHolder).bind(dj)
    }

    inner class BlockedViewHolder(
        private val binding: ListBlockedAccountBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener {
                binding.dj?.let { dj ->
                    showDialog(dj.nickname)
                }
            }
        }

        private fun showDialog(name: String){
            val dlg = CustomDialog(context)
            dlg.show("${name}님을 차단 해제하시겠어요?", "차단 해제")

            dlg.setOnOkClickedListener { content ->
                when(content){
                    "yes" -> {

                    }
                }
            }
        }

        fun bind(item: Profile) {
            binding.apply {
                dj = item
                executePendingBindings()
            }
        }
    }

}

private class BlockedDiffCallback : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem == newItem
    }
}