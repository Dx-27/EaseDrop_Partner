package com.example.easedroppartner.easedroppartner.Api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.Model.Instant_output_model
import com.example.easedroppartner.easedroppartner.Model.OutputModel
import com.example.easedroppartner.easedroppartner.fragment.ActiveFragment
import com.example.easedroppartner.easedroppartner.fragment.CancelFragment
import com.example.easedroppartner.easedroppartner.fragment.HomeFragment
import com.google.gson.Gson
import java.nio.charset.Charset

class Package_Api  {

    public fun getPackage(homeFragment: HomeFragment, uid: String?) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(homeFragment.context)
        val URL: String = ConstantData.serveraddress + ConstantData.instant_url

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel: Instant_output_model =
                    gson.fromJson(response, Instant_output_model::class.java)

//                 Do something with the customerOutputModel
                if (instantOutputModel.status) {
                    instantOutputModel.packages

                    if (true) {
                        homeFragment.getPackage(instantOutputModel)
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
                    homeFragment.context,
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
    public fun AcceptPackage(context: Context, id: String?, lat_lang: String) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val URL: String = ConstantData.serveraddress + ConstantData.accept_ins_Package

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel:Instant_output_model =
                    gson.fromJson(response, Instant_output_model::class.java)

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
        val URL: String = ConstantData.serveraddress + ConstantData.deliveredpackage

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
                  //  (context as ActiveFragment).setadapter(instantOutputModel)


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

    public fun getdeliveredIns_Package(context:ActiveFragment, id:String?) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context.requireContext())
        val URL: String = ConstantData.serveraddress + ConstantData.get_delevered

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel: Instant_output_model =
                    gson.fromJson(response, Instant_output_model::class.java)

//                 Do something with the customerOutputModel
                if (instantOutputModel.status) {
                    //accpted

                    Toast.makeText(context.requireContext(), instantOutputModel.message, Toast.LENGTH_SHORT).show()
                    (context as ActiveFragment).setadapter(instantOutputModel)


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
    public fun getcancelIns_Package(context:CancelFragment, id:String?) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context.requireContext())
        val URL: String = ConstantData.serveraddress + ConstantData.get_cancel

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel: Instant_output_model =
                    gson.fromJson(response, Instant_output_model::class.java)

//                 Do something with the customerOutputModel
                if (instantOutputModel.status) {
                    //accpted

                    Toast.makeText(context.requireContext(), instantOutputModel.message, Toast.LENGTH_SHORT).show()
                    (context as CancelFragment).setadapter(instantOutputModel)


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
        val URL: String = ConstantData.serveraddress + ConstantData.cancelUrl

        val stringRequest = object : StringRequest(Method.POST, URL,
            { response ->
                // Handle the response here
                val gson = Gson()
                val instantOutputModel: OutputModel =
                    gson.fromJson(response,  OutputModel::class.java)

//                 Do something with the customerOutputModel
                if (instantOutputModel.status) {
                    //accpted
                    Toast.makeText(context, instantOutputModel.message, Toast.LENGTH_SHORT).show()
//                    (context as CancelFragment).setadapter(instantOutputModel)
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