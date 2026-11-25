package com.example.easedroppartner.easedroppartner.Api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.easedroppartner.easedroppartner.Model.All_India_Delivery_Model
import com.example.easedroppartner.easedroppartner.Model.All_India_output_Model
import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.Model.Instant_output_model
import com.example.easedroppartner.easedroppartner.Model.OutputModel
import com.example.easedroppartner.easedroppartner.fragment.ActiveFragment
import com.example.easedroppartner.easedroppartner.fragment.All_india_delivery_Fragment
import com.example.easedroppartner.easedroppartner.fragment.CancelFragment
import com.example.easedroppartner.easedroppartner.fragment.Cancel_all_india_Fragment
import com.example.easedroppartner.easedroppartner.fragment.Delivered_all_india_Fragment
import com.example.easedroppartner.easedroppartner.fragment.HomeFragment
import com.google.gson.Gson
import java.nio.charset.Charset

class All_India_delivery_Api  {

    public fun getPackage(allIndiaDeliveryFragment: All_india_delivery_Fragment, uid: String?) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(allIndiaDeliveryFragment.context)
        val URL: String = ConstantData.serveraddress + ConstantData.allindiagetpackagedelivery

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val allIndiaOutputModel: All_India_output_Model =
                    gson.fromJson(response, All_India_output_Model::class.java)

//                 Do something with the customerOutputModel
                if (allIndiaOutputModel.status) {
                    allIndiaOutputModel.packages

                    if (true) {

                        allIndiaDeliveryFragment.getPackage(allIndiaOutputModel)
                    }

                }
            },
            { error ->
                // Handle error here
                if (error.networkResponse != null) {
                    val errorData = String(error.networkResponse.data, Charset.defaultCharset())
                    Log.e("errors", errorData)
                    // Log or handle the errorData
                }
                Toast.makeText(
                    allIndiaDeliveryFragment.context,
                    "Error: ${error.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }) {

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = uid ?: ""
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
    public fun AcceptPackage(context: Context, id: String?,lat_lang: String) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val URL: String = ConstantData.serveraddress + ConstantData.allindiapackageurl

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel:All_India_output_Model =
                    gson.fromJson(response, All_India_output_Model::class.java)

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
            }) {

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = id ?: ""
                params["lat_lang"] = lat_lang
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


    public fun DeliverPackage(context:Context, id:String?) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val URL: String = ConstantData.serveraddress + ConstantData.db_doneindia_pack

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel: OutputModel =
                    gson.fromJson(response,OutputModel::class.java)

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
    public fun getdeliveredindia_Package(context:Delivered_all_india_Fragment, id:String?) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context.requireContext())
        val URL: String = ConstantData.serveraddress + ConstantData.allindiadelivery

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel: All_India_output_Model =
                    gson.fromJson(response, All_India_output_Model::class.java)

//                 Do something with the customerOutputModel
                if (instantOutputModel.status) {
                    //accpted

                    Toast.makeText(context.requireContext(), instantOutputModel.message, Toast.LENGTH_SHORT).show()
                    (context as Delivered_all_india_Fragment).setadapter(instantOutputModel)
                }
            },
            { error ->
                // Handle error here
                if (error.networkResponse != null) {
                    val errorData = String(error.networkResponse.data, Charset.defaultCharset())
                    Log.e("errors", errorData)
                    // Log or handle the errorData
                }
                Toast.makeText(context.requireContext(), "Error: ${error.toString()}", Toast.LENGTH_SHORT).show()
            })
        {

            override fun getParams(): MutableMap<String, String>
            {
                val params = HashMap<String, String>()
                params["did"]=id?:""
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
    public fun getcancelindia_Package(context:Cancel_all_india_Fragment, id:String?) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context.requireContext())
        val URL: String = ConstantData.serveraddress + ConstantData.get_cancelallindia

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel: All_India_output_Model =
                    gson.fromJson(response, All_India_output_Model::class.java)

//                 Do something with the customerOutputModel
                if (instantOutputModel.status) {
                    //accpted

                    Toast.makeText(context.requireContext(), instantOutputModel.message, Toast.LENGTH_SHORT).show()
                    (context as Cancel_all_india_Fragment).setadapter(instantOutputModel)


                }
            },
            { error ->
                // Handle error here
                if (error.networkResponse != null) {
                    val errorData = String(error.networkResponse.data, Charset.defaultCharset())
                    Log.e("errors", errorData)
                    // Log or handle the errorData
                }
                Toast.makeText(context.requireContext(), "Error: ${error.toString()}", Toast.LENGTH_SHORT).show()
            })
        {

            override fun getParams(): MutableMap<String, String>
            {
                val params = HashMap<String, String>()
                params["did"]=id?:""
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


    public fun cancelPackage(context: Context, id:String?) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val URL: String = ConstantData.serveraddress + ConstantData.allindiacancel

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel: OutputModel =
                    gson.fromJson(response, OutputModel::class.java)

//                 Do something with the customerOutputModel
                if (instantOutputModel.status) {
                    //accpted
                    Toast.makeText(context, instantOutputModel.message, Toast.LENGTH_SHORT).show()
                   // (context as Cancel_all_india_Fragment).setadapter(instantOutputModel)


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

            }) {

            override fun getParams(): MutableMap<String, String> {
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