package com.infinity.omos.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infinity.omos.ui.dj.FollowerFragment
import com.infinity.omos.ui.dj.FollowingFragment

const val FOLLOWER_PAGE_INDEX = 0
const val FOLLOWING_PAGE_INDEX = 1

class FollowPagerAdapter(fragmentActivity: FragmentActivity, private val userId: Int):
    FragmentStateAdapter(fragmentActivity) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        FOLLOWER_PAGE_INDEX to { FollowerFragment(userId) },
        FOLLOWING_PAGE_INDEX to { FollowingFragment(userId) }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
