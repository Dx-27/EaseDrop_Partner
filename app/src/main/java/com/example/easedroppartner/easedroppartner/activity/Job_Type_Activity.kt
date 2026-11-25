package com.example.easedroppartner.easedroppartner.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easedroppartner.R

class Job_Type_Activity : AppCompatActivity() {

    private lateinit var lin_instantDelivery: LinearLayout
    private lateinit var lin_all_india_delivery: LinearLayout
    private lateinit var lin_heavy_goods: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(

            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_job_type) // Move setContentView here

        enableEdgeToEdge()

        lin_instantDelivery=findViewById(R.id.lin_instantDelivery)
        lin_all_india_delivery=findViewById(R.id.lin_all_india_delivery)
        lin_heavy_goods=findViewById(R.id.lin_heavy_goods)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lin_instantDelivery.setOnClickListener {
            val intent = Intent(this, Sign_in_Activity::class.java)
            startActivity(intent)
        }
        lin_all_india_delivery.setOnClickListener {
            val intent = Intent(this, All_India_Delivery_Sign_In_Activity::class.java)
            startActivity(intent)
        }
        lin_heavy_goods.setOnClickListener {
            val intent = Intent(this, Sign_in_Activity::class.java)
            startActivity(intent)
        }
    }
}