package com.infinity.omos.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infinity.omos.ui.search.DetailArtistAlbumFragment
import com.infinity.omos.ui.search.DetailArtistMusicFragment

class ArtistViewPagerAdapter(fragmentActivity: FragmentActivity, private val artistId: String) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = listOf(DetailArtistMusicFragment(), DetailArtistAlbumFragment())

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