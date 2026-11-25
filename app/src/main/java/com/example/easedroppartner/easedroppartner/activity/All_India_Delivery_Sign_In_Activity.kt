package com.example.easedroppartner.easedroppartner.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Api.All_india_login_Api
import com.example.easedroppartner.easedroppartner.Model.Login_Model
import com.example.easedroppartner.easedroppartner.adapter.LocaleHelper

class All_India_Delivery_Sign_In_Activity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var appResources: Resources
    private lateinit var logintext: TextView

    private val languageChangeRecevier = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val selectedLanguage = LocaleHelper.getLanguage(p0!!)
            updateUIWithLanguage(selectedLanguage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_india_delivery_sign_in)

        val scrollView = findViewById<ScrollView>(R.id.main)
        val cardLogin = findViewById<View>(R.id.card_login)

        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                maxOf(systemBars.bottom, ime.bottom)
            )

            if (isKeyboardVisible) {
                scrollView.post { scrollView.smoothScrollTo(0, cardLogin.bottom) }
            }
            insets
        }

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        button = findViewById(R.id.btn_login)
        logintext = findViewById(R.id.logintext)

        val selectedLanguage = LocaleHelper.getLanguage(this)
        updateUIWithLanguage(selectedLanguage)

        button.setOnClickListener {
            val userEmail = email.text.toString()
            val userPassword = password.text.toString()
            val login = Login_Model()
            login.email = userEmail
            login.password = userPassword
            All_india_login_Api().logindb(this, login)
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        registerReceiver(
            languageChangeRecevier,
            IntentFilter("com.example.easedrop.ACTION_LANGUAGE_CHANGED")
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(languageChangeRecevier)
    }

    private fun updateUIWithLanguage(languageCode: String) {
        val context = LocaleHelper.setLocale(this, languageCode)
        appResources = context.resources
        button.text = appResources.getString(R.string.btn_login)
        logintext.text = appResources.getString(R.string.logintext)
    }
}