package com.example.easedroppartner.easedroppartner.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.adapter.HomeAdapter
import com.example.easedroppartner.easedroppartner.adapter.LocaleHelper
import com.google.android.material.tabs.TabLayout


class DeliveredFragment : Fragment() {

    lateinit var ordertab: TabLayout
    lateinit var orderviewpager: ViewPager
    private lateinit var tv_name: TextView
    private lateinit var tv_address: TextView
    private lateinit var tv_phone: TextView
    private lateinit var tv_payment_type: TextView
    private lateinit var tv_price: TextView
    private lateinit var tv_total:TextView
    private lateinit var appResources: Resources

    private val languageChangeReceiver=object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val selectedLanguage=LocaleHelper.getLanguage(context!!)
            updateUIWithLanguage(selectedLanguage)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_delivered, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        tv_name=view.findViewById(R.id.tv_name)
//        tv_address=view.findViewById(R.id.tv_address)
//        tv_phone=view.findViewById(R.id.tv_phone)
//        tv_payment_type=view.findViewById(R.id.tv_payment_type)
//        tv_price=view.findViewById(R.id.tv_price)
//        tv_total=view.findViewById(R.id.tv_total)
        ordertab=view.findViewById(R.id.ordertab)
        orderviewpager=view.findViewById(R.id.orderviewpager)

//        val selectedLanguage= LocaleHelper.getLanguage(requireContext())
//        updateUIWithLanguage(selectedLanguage)

    }

    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(
            languageChangeReceiver, IntentFilter("com.example.easedrop.ACTION_LANGUAGE_CHANGED")
        )
    }

//    override fun onPause() {
//        super.onPause()
//        requireContext().unregisterReceiver(languageChangeReceiver)
//    }

//    private fun updateUIWithLanguage(languageCode: String) {
//        // Update UI elements with the new language
//        val context = LocaleHelper.setLocale(requireContext(), languageCode)
//        appResources = context.resources
//        tv_name.text = appResources.getString(R.string.name_tv)
//        tv_address.text = appResources.getString(R.string.tv_address)
//        tv_phone.text = appResources.getString(R.string.tv_phone)
//        tv_payment_type.text = appResources.getString(R.string.tv_payment_type)
//        tv_price.text = appResources.getString(R.string.tv_price)
//        tv_total.text = appResources.getString(R.string.tv_total)
//    }
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

    private fun updateUIWithLanguage(languageCode: String) {
        // Update UI elements with the new language
        val context = LocaleHelper.setLocale(requireContext(), languageCode)
        appResources = context.resources
        tv_name.text = appResources.getString(R.string.name_tv)
        tv_address.text = appResources.getString(R.string.tv_address)
        tv_phone.text = appResources.getString(R.string.tv_phone)
        tv_payment_type.text = appResources.getString(R.string.tv_payment_type)
        tv_price.text = appResources.getString(R.string.tv_price)
        tv_total.text = appResources.getString(R.string.tv_total)
    }
    private fun setUpViewPager(orderviewpager: ViewPager) {
        val adapter = HomeAdapter(childFragmentManager)  // Use childFragmentManager
        adapter.addFragment(ActiveFragment(), "Delivered Order")
        adapter.addFragment(CancelFragment(), "Cancel Order")
        orderviewpager.adapter = adapter  // Use adapter instead of setAdapter
        }
}