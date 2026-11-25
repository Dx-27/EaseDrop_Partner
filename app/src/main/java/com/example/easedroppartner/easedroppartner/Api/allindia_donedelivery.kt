package com.example.easedroppartner.easedroppartner.Api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.Model.OutputModel
import com.google.gson.Gson
import java.nio.charset.Charset

class allindia_donedelivery {
    public fun DeliverPackage(context: Context, id:String?) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val URL: String = ConstantData.serveraddress + ConstantData.db_doneindia_pack

        val stringRequest = object : StringRequest(
            Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel: OutputModel =
                    gson.fromJson(response, OutputModel::class.java)

//                 Do something with the customerOutputModel
                if (instantOutputModel.status) {
                    //accpted
                    Toast.makeText(context, instantOutputModel.message, Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                // Handle error here
                if (error.networkResponse != null) {
                    val errorData = String(error.networkResponse.data, Charset.defaultCharset())
                    Log.e("errors", errorData)
                    // Log or handle the errorData
                }
                Toast.makeText(context, "Error: ${error.toString()}", Toast.LENGTH_SHORT).show()
            })
        {

            override fun getParams(): MutableMap<String, String>
            {
                val params = HashMap<String, String>()
                params["id"]=id?:""
                return params
            }
        }
        requestQueue.add(stringRequest)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            5000,  // Socket timeout in milliseconds
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
    }

}