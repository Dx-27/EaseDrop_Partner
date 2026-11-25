package com.example.easedroppartner.easedroppartner.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.fragment.All_india_canel_delivered_tab_Fragment
import com.example.easedroppartner.easedroppartner.fragment.All_india_delivery_Fragment
import com.example.easedroppartner.easedroppartner.fragment.All_india_profile_Fragment
import com.example.easedroppartner.easedroppartner.fragment.DeliveredFragment
import com.example.easedroppartner.easedroppartner.fragment.HomeFragment
import com.example.easedroppartner.easedroppartner.fragment.ProfileFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.material.appbar.AppBarLayout
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import es.dmoral.toasty.Toasty
import java.util.Locale

class All_India_Home_Activity : AppCompatActivity()
{

//private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var btmNav: ChipNavigationBar
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val DELAY:Long=4000
    private val SPLASH_DELAY:Long=3000

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_india_home)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT
        enableEdgeToEdge()

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets

       // toolbar=findViewById(R.id.toolbar)
        appBarLayout=findViewById(R.id.appBarLayout)
        btmNav=findViewById(R.id.bottom_nav_bar)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(
                this,


                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 100)
        }
        getCurrentLocation()


        switchFragment(HomeFragment())

        btmNav.setOnItemSelectedListener {
            when (it) {
                R.id.menu_All_india_home -> switchFragment(All_india_delivery_Fragment())
                R.id.menu_All_india_deliver -> switchFragment(All_india_canel_delivered_tab_Fragment())
                R.id.menu_All_india_profile -> switchFragment(All_india_profile_Fragment())
            }
            // Return true to indicate the item is selected
        }

    }
    //Switch Fragments
    public fun switchFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment, null)
        fragmentTransaction.commit()
    }



    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                )
                {
                    return
                }

                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location = task.result
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses: MutableList<Address>? = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                    ConstantData.KEY_LAT_LONG=location.latitude.toString()+  "," + location.longitude
                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {

                            val city = addresses[0].locality
                            val pin = addresses[0].postalCode
                            val state = addresses[0].adminArea
                            val locality = addresses[0].getAddressLine(0)

                            //Toast.makeText(this, "loc: $user_address", Toast.LENGTH_LONG).show()

                        }
                    }

                }

            } else {
                //settings
                val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                    .build()

                val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)

                val task =
                    LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

                task.addOnFailureListener {

                    if (it is ResolvableApiException) {

                        try {
                            val intentSenderRequest =
                                IntentSenderRequest.Builder(it.resolution).build()
                            resultLauncher.launch(intentSenderRequest)
                        } catch (exception: IntentSender.SendIntentException) {
                            Log.e(ContentValues.TAG, "startGps: ${exception.message}")
                        }
                    }
                }
            }
        } else {
            //request
            requestPermission()
        }
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->

        if (activityResult.resultCode == RESULT_OK) {
            Toast.makeText(this, "Gps is on", Toast.LENGTH_SHORT).show()
            val view = View.inflate(this, R.layout.dialog, null)
            val buil = AlertDialog.Builder(this)
            buil.setView(view)
            val dialog = buil.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            android.os.Handler().postDelayed({
                Toasty.success(
                    this,
                    "Thanks! for waiting  We've got your location.",
                    Toast.LENGTH_SHORT,
                    true
                ).show();
                dialog.dismiss()
            }, SPLASH_DELAY)
        } else if (activityResult.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Request is Canceled", Toast.LENGTH_SHORT).show()
        }

    }
    private fun isLocationEnabled(): Boolean {

        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Access", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                val view = View.inflate(this, R.layout.allow_loc_dialog, null)
                val buil = AlertDialog.Builder(this)
                buil.setView(view)
                val dialog = buil.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                android.os.Handler().postDelayed({
                    Toasty.success(this,"Thanks! for waiting. We've got your location.", Toasty.LENGTH_SHORT).show()

                    dialog.dismiss()

                    // After the delay, check location settings
                    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                        .build()

                    val builder = LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)

                    val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

                    task.addOnFailureListener {
                        if (it is ResolvableApiException) {
                            try
                            {
                                val intentSenderRequest =
                                    IntentSenderRequest.Builder(it.resolution).build()
                                resultLauncher.launch(intentSenderRequest)
                            } catch (exception: IntentSender.SendIntentException) {
                                Log.e(ContentValues.TAG, "startGps: ${exception.message}")
                            }
                        }
                    }
                }, DELAY)

                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()

            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100 }

}