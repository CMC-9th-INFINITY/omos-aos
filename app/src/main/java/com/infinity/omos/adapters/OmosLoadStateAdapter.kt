package com.infinity.omos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.databinding.LayoutProgressBinding

class OmosLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<OmosLoadStateAdapter.OmosLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): OmosLoadStateViewHolder {
        return OmosLoadStateViewHolder(
            LayoutProgressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OmosLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class OmosLoadStateViewHolder(
        private val binding: LayoutProgressBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {

        }
    }
}