package com.example.easedroppartner.easedroppartner.activity

import com.example.easedroppartner.easedroppartner.Api.Login_api
import com.example.easedroppartner.easedroppartner.Model.Login_Model
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.adapter.LocaleHelper

class Sign_in_Activity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var appResources: Resources
    private lateinit var logintext: TextView

    private val languageChangeRecevier = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val selectedLanguage=LocaleHelper.getLanguage(p0!!)
            updateUIWithLanguage(selectedLanguage)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT
        enableEdgeToEdge()


        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        button = findViewById(R.id.btn_login)
        logintext = findViewById(R.id.logintext)

        val selectedLanguage = LocaleHelper.getLanguage(this)
        updateUIWithLanguage(selectedLanguage)

//        ed_login_user_id=findViewById(R.id.ed_login_user_id)
//        ed_login_user_password=findViewById(R.id.ed_login_user_Password)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        val scrollView = findViewById<ScrollView>(R.id.main_scroll)
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
        button.setOnClickListener {
            email = findViewById(R.id.email)
            password = findViewById(R.id.password)
            val email = email.text.toString()
            val password = password.text.toString()
            val login = Login_Model()
            login.email = email
            login.password = password
            Login_api().logindb(this, login)
//            val c_id=ed_login_user_id.text.toString()
//            val c_password=ed_login_user_password.text.toString()
//            if (c_id.isEmpty() || c_password.isEmpty())
//            {
//                Toasty.warning(this, "Empty fields are not allowed", Toasty.LENGTH_SHORT).show()
//                if (c_id.isEmpty()){
//                    ed_login_user_id.error="please enter your User Id"
//                }
//                if (c_password.isEmpty()){
//                    ed_login_user_password.error="please enter your Password"
//                }
//
//            }
//            else if (c_password.length < 6){
//                Toasty.info(this, "Password should be at least 6 characters", Toasty.LENGTH_SHORT).show()
//
//            }
//            else{
//                val login = Login_Model()
//                login.id = c_id
//                login.password = c_password
//                LoginApi().loginUser(this, login)
//            }
//            val intent= Intent(this@Sign_in_Activity, HomeActivity::class.java)
//            finish()
//            startActivity(intent)
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
        // Update UI elements with the new language
        val context = LocaleHelper.setLocale(this, languageCode)
        appResources = context.resources
        button.text = appResources.getString(R.string.btn_login)
        logintext.text = appResources.getString(R.string.logintext)
    }
}