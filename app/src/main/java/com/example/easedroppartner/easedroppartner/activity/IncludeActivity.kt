package com.example.easedroppartner.easedroppartner.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.easedroppartner.R

class IncludeActivity : AppCompatActivity() {

//    private  lateinit var call1: LinearLayout

//    private  lateinit var call2: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_include)
        enableEdgeToEdge()


        // dialcall code

//        call1=findViewById(R.id.call1)
//        call1.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.setData(Uri.parse("tel:111111111"))
//            finish()
//            startActivity(intent)
//        }
//        call2=findViewById(R.id.call2)
//
//        call2.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.setData(Uri.parse("tel:111111111"))
//            finish()
//            startActivity(intent)
//        }
    }
}