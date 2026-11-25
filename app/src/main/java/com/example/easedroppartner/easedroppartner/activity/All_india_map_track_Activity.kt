package com.example.easedroppartner.easedroppartner.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Api.All_India_delivery_Api
import com.example.easedroppartner.easedroppartner.Api.All_india_login_Api
import com.example.easedroppartner.easedroppartner.Api.Package_Api
import com.example.easedroppartner.easedroppartner.Api.allindia_donedelivery
import com.example.easedroppartner.easedroppartner.Model.All_India_Delivery_Model
import com.example.easedroppartner.easedroppartner.Model.Instant_model
import com.example.easedroppartner.easedroppartner.Model.Login_Model
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.errors.ApiException
import com.google.maps.model.TravelMode
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class All_india_map_track_Activity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var tv_payment_type: TextView
    private lateinit var tv_total_amt: TextView
    private lateinit var tv_sender_name: TextView
    private lateinit var tv_sender_address: TextView
    private lateinit var img_sender_phone: ImageView
    private lateinit var tv_package_type: TextView
    private lateinit var tv_frangible: TextView
    private lateinit var tv_size: TextView
    private lateinit var tv_package_waight: TextView
    private lateinit var tv_km: TextView
    private var currentLocationMarker: Marker? = null
    private var senderMarker: Marker? = null

    private var polyline: Polyline? = null
    private var geoApiContext: GeoApiContext? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    //private lateinit var tv_desc:TextView
    private lateinit var btn_delivered: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btn_start_navigation: ImageView
    private lateinit var ed_otp: EditText
    private lateinit var sender_lat_lng: TextView
    private var senderLatLng = LatLng(0.0, 0.0)
    private var receiverLatLng = LatLng(0.0, 0.0)

    private val REQUEST_LOCATION_PERMISSION = 1001
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_india_map_track)
        val All_India_Delivery_Model = intent.getSerializableExtra("package") as? All_India_Delivery_Model
        val model = Login_Model()
        tv_payment_type = findViewById(R.id.tv_payment_type)
        tv_total_amt = findViewById(R.id.tv_total_amt)
        tv_sender_name = findViewById(R.id.tv_sender_name)
        tv_sender_address = findViewById(R.id.tv_sender_address)
        img_sender_phone = findViewById(R.id.img_sender_phone)
        tv_package_type = findViewById(R.id.tv_package_type)
        tv_frangible = findViewById(R.id.tv_frangible)
        btn_start_navigation = findViewById(R.id.btn_start_navigation)
        tv_km=findViewById(R.id.tv_km)
        tv_package_waight = findViewById(R.id.tv_package_waight)
        btn_delivered = findViewById(R.id.btn_delivered)
        sender_lat_lng=findViewById(R.id.sender_lat_lng)

        ed_otp=findViewById(R.id.ed_otp)

        senderLatLng = LatLng(
            (All_India_Delivery_Model?.sender_lat_lang ?: 0.0) as Double,
            (All_India_Delivery_Model?.sender_lat_lang ?: 0.0) as Double
        )
        receiverLatLng = LatLng(
            ((model.lat_lang ?: 0.0) as Double),
            ((model.lat_lang ?: 0.0) as Double)
        )
        tv_payment_type.text = All_India_Delivery_Model?.payment_type
        tv_total_amt.text = All_India_Delivery_Model?.total_amt
        tv_sender_name.text = All_India_Delivery_Model?.sender_name
        tv_sender_address.text = All_India_Delivery_Model?.sender_add

        tv_package_waight.text = All_India_Delivery_Model?.package_weight
        //tv_desc.text=packageModel?.package_desc


        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT
        enableEdgeToEdge()

        img_sender_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + All_India_Delivery_Model?.sender_ph)
            startActivity(intent)
        }
        btn_delivered.setOnClickListener {
            //create delivered button
            val api=allindia_donedelivery()
            api.DeliverPackage(this, All_India_Delivery_Model!!.id)
        }

        btn_start_navigation.setOnClickListener {
            startNavigation()
        }

        // Initialize the mapView
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        geoApiContext = GeoApiContext.Builder()
            .apiKey("AIzaSyAkrg06v37neAL-jaRQR7OcJ-yuyP6fKDE")
            .build()

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initMap()
        initLocationUpdates()
    }


    private fun initMap() {
        mapView.getMapAsync { map ->
            googleMap = map
            enableLocation()
            enableMyLocation()
            drawInitialRoute()
            addSenderMarker()
            addReceiverMarker()
            addCurrentLocationMarker()
        }
    }


    private fun startNavigation() {
        if (hasLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    drawRoute(currentLatLng, senderLatLng)
                    drawRoute(senderLatLng, receiverLatLng)

                    // Calculate distances
                    val distanceToSender = calculateDistance(currentLatLng, senderLatLng)
                    val distanceToReceiver = calculateDistance(senderLatLng, receiverLatLng)
                    val totalDistance = distanceToSender + distanceToReceiver

                    // Format distance to one decimal point
                    val formattedDistance = String.format("%.1f", totalDistance)

                    // Update TextView
                    tv_km.text = formattedDistance+"km"
                }
            }
        }
    }

    private fun calculateDistance(origin: LatLng, destination: LatLng): Double {
        val results = FloatArray(1)
        Location.distanceBetween(
            origin.latitude, origin.longitude,
            destination.latitude, destination.longitude,
            results
        )
        // Convert meters to kilometers
        return results[0].toDouble() / 1000
    }



    private fun enableMyLocation() {
        if (hasLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            googleMap.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun enableLocation() {
        if (hasLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                        2000,
                        null
                    )
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun initLocationUpdates() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { updateCurrentLocation(it) }
            }
        }

        locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun startLocationUpdates() {
        if (hasLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateCurrentLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (currentLocationMarker == null) {
            currentLocationMarker = googleMap.addMarker(
                MarkerOptions().position(latLng).title("Current Location")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } else {
            currentLocationMarker?.position = latLng
        }

        drawRoute(latLng, receiverLatLng)
    }

    private fun drawRoute(origin: LatLng, destination: LatLng) {
        polyline?.remove()
        GlobalScope.launch(Dispatchers.Main) {
            val directionsResult = withContext(Dispatchers.IO) {
                try {
                    DirectionsApi.newRequest(geoApiContext)
                        .mode(TravelMode.DRIVING)
                        .origin("${origin.latitude},${origin.longitude}")
                        .destination("${destination.latitude},${destination.longitude}")
                        .await()
                } catch (e: ApiException) {
                    e.printStackTrace()
                    null
                }
            }

            if (directionsResult != null && directionsResult.routes.isNotEmpty()) {
                val decodedPath = directionsResult.routes[0].overviewPolyline.decodePath()
                val points = ArrayList<LatLng>()
                for (point in decodedPath) {
                    points.add(LatLng(point.lat, point.lng))
                }
                val options = PolylineOptions()
                    .addAll(points)
                    .color(ContextCompat.getColor(this@All_india_map_track_Activity, R.color.bl))
                polyline = googleMap.addPolyline(options)
            } else {
                Log.e("Directions", "Failed to get directions")
            }
        }
    }

    private fun drawInitialRoute() {
        drawRoute(senderLatLng, receiverLatLng)
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation()
                    enableLocation()
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Set the map type to satellite


        drawRoute(senderLatLng, receiverLatLng)
        addSenderMarker()
        addReceiverMarker()
        addCurrentLocationMarker()

        if (hasLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                        2000,
                        null
                    )
                }
            }
        }
    }


    private fun addSenderMarker() {
        senderMarker = googleMap.addMarker(
            MarkerOptions()
                .position(senderLatLng)
                .title("Sender address")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
    }

    private fun addReceiverMarker() {
        var receiverMarker = googleMap.addMarker(
            MarkerOptions()
                .position(receiverLatLng)
                .title("Receiver address")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )
    }

    private fun addCurrentLocationMarker() {
        // This method should be called only after obtaining the current location
        // You can call it in the success listener of fusedLocationClient.lastLocation
        // For simplicity, I'm just adding it here
        currentLocationMarker = googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0)) // Placeholder coordinates
                .title("Current Location")
        )
    }

}