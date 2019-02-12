package com.example.testtinkoff.view

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.testtinkoff.R
import com.example.testtinkoff.view.listPoints.ListPointsFragment
import com.example.testtinkoff.view.map.MapFragment

class PointsFragmentPagerAdapter(fm: FragmentManager, private val context: Context): FragmentPagerAdapter(fm) {
    companion object {
        private const val PAGE_COUNT = 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MapFragment.newInstance()
            1 -> ListPointsFragment.newInstance()
            else -> MapFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.resources.getString(R.string.tab_title_map)
            1 -> context.resources.getString(R.string.tab_title_list)
            else -> ""
        }
    }

    override fun getCount(): Int {
        return PAGE_COUNT
    }
}