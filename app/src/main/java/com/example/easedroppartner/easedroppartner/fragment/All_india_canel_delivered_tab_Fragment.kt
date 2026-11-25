package com.example.easedroppartner.easedroppartner.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.adapter.All_india_Home_Adapter
import com.example.easedroppartner.easedroppartner.adapter.HomeAdapter
import com.google.android.material.tabs.TabLayout


class All_india_canel_delivered_tab_Fragment : Fragment() {

    lateinit var ordertab: TabLayout
    lateinit var orderviewpager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_all_india_canel_delivered_tab_, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordertab = view.findViewById(R.id.ordertab)
        orderviewpager = view.findViewById(R.id.orderviewpager)
    }
    private fun setUpViewPager(orderviewpager: ViewPager) {
        val adapter = All_india_Home_Adapter(childFragmentManager)  // Use childFragmentManager
        adapter.addFragment(Delivered_all_india_Fragment(), "Delivered Order")
        adapter.addFragment(Cancel_all_india_Fragment(), "Cancel Order")
        orderviewpager.adapter = adapter  // Use adapter instead of setAdapter
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewPager(orderviewpager)
        ordertab.setupWithViewPager(orderviewpager)

        ordertab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab selection
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselection
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselection
            }
        })
    }

}