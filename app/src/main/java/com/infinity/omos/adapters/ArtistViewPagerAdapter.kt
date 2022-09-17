package com.infinity.omos.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infinity.omos.ui.search.ArtistAlbumFragment
import com.infinity.omos.ui.search.ArtistMusicFragment

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