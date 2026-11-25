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

class CancelFragment : Fragment() {

    lateinit var recycle_cancelorder: RecyclerView

    private  lateinit var view :View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_cancel, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycle_cancelorder=view.findViewById(R.id.recycle_cancelorder)

        val sharedPreferences = requireActivity().getSharedPreferences(ConstantData.SP, Context.MODE_PRIVATE)
        val id=sharedPreferences.getString(ConstantData.KEY_ID,"")
        Package_Api().getcancelIns_Package(this,id)
    }
    public fun setadapter(packageOutputModel : Instant_output_model){

        val adapter= packageOutputModel.packages?.let { PackageAdapter(it,requireContext()) }

        recycle_cancelorder.layoutManager= LinearLayoutManager(context)
        recycle_cancelorder.adapter=adapter
    }
}