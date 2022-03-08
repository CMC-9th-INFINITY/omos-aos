package com.infinity.omos.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infinity.omos.ui.searchtab.*

class ArtistViewPagerAdapter(fragmentActivity: FragmentActivity, private val artistId: String) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = listOf(ArtistMusicFragment(), ArtistAlbumFragment())

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        var bundle = Bundle()
        bundle.putString("artistId", artistId)
        fragmentList[position].arguments = bundle
        return fragmentList[position]
    }
}