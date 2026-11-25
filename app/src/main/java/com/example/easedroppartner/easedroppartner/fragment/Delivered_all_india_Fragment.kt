package com.example.easedroppartner.easedroppartner.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Api.All_India_delivery_Api
import com.example.easedroppartner.easedroppartner.Api.Package_Api
import com.example.easedroppartner.easedroppartner.Model.All_India_output_Model
import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.Model.Instant_output_model
import com.example.easedroppartner.easedroppartner.adapter.All_India_Delivery_Adapter
import com.example.easedroppartner.easedroppartner.adapter.All_india_Delivered_Adapter


class Delivered_all_india_Fragment : Fragment() {

    lateinit var rec_all_india_delivered: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val sharedPreferences = requireActivity().getSharedPreferences(ConstantData.SP, Context.MODE_PRIVATE)
        val id=sharedPreferences.getString(ConstantData.KEY_ID,"")
        //rec_all_india_delivered=view.findViewById(R.id.rec_all_india_delivered)
      //  All_India_delivery_Api().DeliverPackage(this,id)


        return inflater.inflate(R.layout.fragment_delivered_all_india_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rec_all_india_delivered=view.findViewById(R.id.rec_all_india_delivered)
        val sharedPreferences = requireActivity().getSharedPreferences(ConstantData.SP, Context.MODE_PRIVATE)
        val id=sharedPreferences.getString(ConstantData.KEY_ID,"")
        All_India_delivery_Api().getdeliveredindia_Package(this,id)
    }

    public fun setadapter(packageOutputModel : All_India_output_Model) {
        if (packageOutputModel.packages.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No packages available", Toast.LENGTH_SHORT).show()
        } else {
            val All_india_Delivered_Adapter = packageOutputModel.packages?.let {
                All_india_Delivered_Adapter(
                    it,
                    requireContext()
                )
            }

            rec_all_india_delivered.layoutManager = LinearLayoutManager(context)
            rec_all_india_delivered.adapter =All_india_Delivered_Adapter
        }
    }

    }

