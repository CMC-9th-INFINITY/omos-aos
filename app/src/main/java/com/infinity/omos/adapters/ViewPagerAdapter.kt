package com.infinity.omos.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infinity.omos.ui.searchtab.AlbumFragment
import com.infinity.omos.ui.searchtab.AllFragment
import com.infinity.omos.ui.searchtab.ArtistFragment
import com.infinity.omos.ui.searchtab.MusicFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = listOf(AllFragment(), MusicFragment(), AlbumFragment(), ArtistFragment())

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getFragment(): AllFragment{
        return fragmentList[0] as AllFragment
    }
}