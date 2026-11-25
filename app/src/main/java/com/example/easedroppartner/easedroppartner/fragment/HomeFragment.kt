package com.example.easedroppartner.easedroppartner.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Api.Package_Api
import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.Model.Instant_output_model
import com.example.easedroppartner.easedroppartner.adapter.PackageAdapter

class HomeFragment : Fragment() {

//    private lateinit var accept: Button
//    private lateinit var cancel: Button

    lateinit var rec_active_order:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        rec_active_order=view.findViewById(R.id.rec_active_order)

        val packageApi = Package_Api()
        val sharedPreferences =requireActivity().getSharedPreferences(ConstantData.SP, Context.MODE_PRIVATE)
        val uid=sharedPreferences.getString(ConstantData.KEY_ID,"0")
        packageApi.getPackage(this,uid)



    }
    public fun  getPackage(p: Instant_output_model){
        val packageAdapter= p.packages?.let { PackageAdapter(it,requireContext()) };
        rec_active_order.layoutManager = LinearLayoutManager(requireContext())
        rec_active_order.adapter=packageAdapter
    }
}