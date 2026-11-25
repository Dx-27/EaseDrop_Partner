package com.example.easedroppartner.easedroppartner.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT

class HomeAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentArrayList = mutableListOf<Fragment>()
    private val fragmentTitle = mutableListOf<String>() // Change to String

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentArrayList.add(fragment)
        fragmentTitle.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position] // Return the title directly
        }

}