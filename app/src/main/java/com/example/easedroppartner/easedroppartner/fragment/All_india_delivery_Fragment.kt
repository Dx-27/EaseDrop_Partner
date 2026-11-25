package com.example.easedroppartner.easedroppartner.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Api.All_India_delivery_Api
import com.example.easedroppartner.easedroppartner.Api.Package_Api
import com.example.easedroppartner.easedroppartner.Model.All_India_output_Model
import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.Model.Instant_output_model
import com.example.easedroppartner.easedroppartner.adapter.All_India_Delivery_Adapter
import com.example.easedroppartner.easedroppartner.adapter.HomeAdapter
import com.google.android.material.tabs.TabLayout
import es.dmoral.toasty.Toasty

class All_india_delivery_Fragment : Fragment() {

    lateinit var all_india_delivery_order: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_india_delivery_, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        all_india_delivery_order=view.findViewById(R.id.all_india_delivery_order)

        val allIndiaDeliveryApi = All_India_delivery_Api()
        val sharedPreferences =requireActivity().getSharedPreferences(ConstantData.SP, Context.MODE_PRIVATE)
        val uid=sharedPreferences.getString(ConstantData.KEY_ID,"0")
        allIndiaDeliveryApi.getPackage(this,uid)
}
    public fun  getPackage(p: All_India_output_Model){
        val All_India_Delivery_Adapter= p.packages?.let { All_India_Delivery_Adapter(it,requireContext()) };
        all_india_delivery_order.layoutManager = LinearLayoutManager(requireContext())
        all_india_delivery_order.adapter=All_India_Delivery_Adapter
    }
}