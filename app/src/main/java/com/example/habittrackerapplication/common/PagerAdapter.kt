package com.example.habittrackerapplication.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.habittrackerapplication.ui.fragments.TabViewOneFragment

class PagerAdapter(fm: FragmentManager, val f1: Fragment, val f2: Fragment, val f3: Fragment) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3;
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return f1
            1 -> return f2
            2 -> return f3
            else -> {
                return TabViewOneFragment.newInstance()
            }
        }
        // val s=TabViewOneFragment.newInstance().getDay()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "daily"
            }
            1 -> {
                return "weekly"
            }
            2 -> {
                return "monthly"
            }
        }
        return super.getPageTitle(position)
    }

}