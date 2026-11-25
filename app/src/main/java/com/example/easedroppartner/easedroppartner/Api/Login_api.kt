package com.example.easedroppartner.easedroppartner.Api

import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.Model.Login_Model
import com.example.easedroppartner.easedroppartner.Model.Login_Output_Model
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.easedroppartner.easedroppartner.activity.HomeActivity
import com.google.gson.Gson

class Login_api {

    fun logindb(activity: Activity, loginModel: Login_Model) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(activity)
        val URL: String = ConstantData.serveraddress + ConstantData.loginUrl
        val stringRequest = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener { response ->
                val gson = Gson()
                val Login_Output_Model = gson.fromJson(response, Login_Output_Model::class.java)

                if (Login_Output_Model.status) {
                    Toast.makeText(activity, "Login Successfully", Toast.LENGTH_SHORT).show()
                    val sharedPreferences = activity.getSharedPreferences(ConstantData.SP, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    with(editor) {
                        putString(ConstantData.KEY_ID, Login_Output_Model.user?.id)
                        putString(ConstantData.KEY_EMAIL, Login_Output_Model.user?.email)
                        putString(ConstantData.KEY_PASSWORD, Login_Output_Model.user?.password)
                        putString(ConstantData.KEY_PIC, Login_Output_Model.user?.pic)
                        putString(ConstantData.KEY_ADDRESS, Login_Output_Model.user?.address)
                        putString(ConstantData.KEY_BIRTHDATE, Login_Output_Model.user?.birthdate)
                        putString(ConstantData.KEY_NAME, Login_Output_Model.user?.name)
                        putString(ConstantData.KEY_PHONE, Login_Output_Model.user?.phone)
                        putString(ConstantData.KEY_ADHAR_NO, Login_Output_Model.user?.adhar_no)
                        putString(ConstantData.KEY_GENDER, Login_Output_Model.user?.gender)
                        putString(ConstantData.KEY_LIC_NO, Login_Output_Model.user?.lic_no)
                        putBoolean(ConstantData.IS_LOGIN, true)
                        commit()
                        activity.startActivity(Intent(activity, HomeActivity::class.java))
                        activity.finishAffinity()
                    }
                } else {
                    Toast.makeText(activity,Login_Output_Model.message , Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
            }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["email"] = loginModel.email ?: ""
                map["password"] = loginModel.password ?: ""
                return map
            }
        }
        requestQueue.add(stringRequest)
    }

}