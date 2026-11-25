package com.example.easedroppartner.easedroppartner.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easedroppartner.R

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        val SPLASH_DELAY: Long = 3000
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT

        enableEdgeToEdge()


        Handler().postDelayed({
            val intent = Intent(this, Job_Type_Activity::class.java)
            startActivity(intent)
            Animatoo.animateShrink(this)
            finish()
        }, SPLASH_DELAY)

    }
}