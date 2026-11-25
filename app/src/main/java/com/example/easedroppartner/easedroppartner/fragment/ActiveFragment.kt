package com.example.easedroppartner.easedroppartner.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Api.Package_Api
import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.Model.Instant_output_model
import com.example.easedroppartner.easedroppartner.adapter.PackageAdapter

class ActiveFragment : Fragment() {

    private lateinit var tv_name: TextView
    private lateinit var tv_address: TextView
    private lateinit var tv_phone: TextView
    private lateinit var tv_payment_type: TextView
    private lateinit var tv_price: TextView
    private lateinit var tv_total: TextView
    private lateinit var appResources: Resources

    lateinit var recycle_activeorder: RecyclerView

//    private val languageChangeReceiver=object : BroadcastReceiver(){
//        override fun onReceive(p0: Context?, p1: Intent?) {
//            val selectedLanguage= LocaleHelper.getLanguage(context!!)
//            updateUIWithLanguage(selectedLanguage)
//        }
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_active, container, false)
        return view
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences(ConstantData.SP, Context.MODE_PRIVATE)
        val id=sharedPreferences.getString(ConstantData.KEY_ID,"")
        recycle_activeorder=view.findViewById(R.id.recycle_activeorder)
        Package_Api().getdeliveredIns_Package(this,id)

//        tv_name=view.findViewById(R.id.tv_name)
//        tv_address=view.findViewById(R.id.tv_address)
//        tv_phone=view.findViewById(R.id.tv_phone)
//        tv_payment_type=view.findViewById(R.id.tv_payment_type)
//        tv_price=view.findViewById(R.id.tv_price)
//        tv_total=view.findViewById(R.id.tv_total)
//         val selectedLanguage= LocaleHelper.getLanguage(requireContext())
//        updateUIWithLanguage(selectedLanguage)

    }
   // @SuppressLint("UnspecifiedRegisterReceiverFlag")
//    override fun onResume() {
//        super.onResume()
//        requireContext().registerReceiver(
//            languageChangeReceiver, IntentFilter("com.example.easedrop.ACTION_LANGUAGE_CHANGED")
//        )
//    }

//    override fun onPause() {
//        super.onPause()
//        requireContext().unregisterReceiver(languageChangeReceiver)
//    }
//
//        private fun updateUIWithLanguage(languageCode: String) {
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

    public fun setadapter(packageOutputModel : Instant_output_model) {

        if (packageOutputModel.packages.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No packages available", Toast.LENGTH_SHORT).show()
        } else {
            val adapter = PackageAdapter(packageOutputModel.packages!!, requireContext())
            recycle_activeorder.layoutManager = LinearLayoutManager(context)
            recycle_activeorder.adapter = adapter
        }
    }


}