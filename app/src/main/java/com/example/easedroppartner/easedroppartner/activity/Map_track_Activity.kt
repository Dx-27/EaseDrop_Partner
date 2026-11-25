package com.example.easedroppartner.easedroppartner.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Api.Package_Api
import com.example.easedroppartner.easedroppartner.Model.Instant_model
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.util.Locale

class Map_track_Activity : AppCompatActivity() {

    companion object {
        private const val TAG = "RoutesAPI"
        private const val ROUTES_API_URL =
            "https://routes.googleapis.com/directions/v2:computeRoutes"
        private const val ROUTES_FIELD_MASK =
            "routes.polyline.encodedPolyline,routes.distanceMeters,routes.duration"

        private const val LOCATION_PERMISSION_REQUEST = 1001
        private const val CURRENT_ROUTE_MIN_REFRESH_MS = 20_000L
        private const val CURRENT_ROUTE_MAX_REFRESH_MS = 90_000L
        private const val CURRENT_ROUTE_MIN_MOVEMENT_METERS = 50f
    }

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var tv_payment_type: TextView
    private lateinit var tv_total_amt: TextView
    private lateinit var tv_resiver_name: TextView
    private lateinit var tv_resiver_address: TextView
    private lateinit var img_resiver_phone: ImageView
    private lateinit var tv_sender_name: TextView
    private lateinit var tv_sender_address: TextView
    private lateinit var img_sender_phone: ImageView
    private lateinit var tv_package_type: TextView
    private lateinit var tv_frangible: TextView
    private lateinit var tv_size: TextView
    private lateinit var tv_package_waight: TextView
    private lateinit var tv_km: TextView
    private lateinit var btn_delivered: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btn_start_navigation: ImageView
    private lateinit var ed_otp: EditText
    private lateinit var sender_lat_lng: TextView
    private lateinit var receiver_lat_lng: TextView

    private var currentLocationMarker: Marker? = null
    private var senderMarker: Marker? = null
    private var receiverMarker: Marker? = null
    private var riderDirectionIcon: com.google.android.gms.maps.model.BitmapDescriptor? = null
    private var lastRiderHeadingDegrees: Float = 0f

    // Keep the two route segments separate. Updating one will not remove the other.
    private var currentToSenderPolyline: Polyline? = null
    private var senderToReceiverPolyline: Polyline? = null

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private var senderLatLng = LatLng(0.0, 0.0)
    private var receiverLatLng = LatLng(0.0, 0.0)
    private var latestLocation: Location? = null

    // Prevent an older, slower network response from overwriting a newer route.
    private var currentToSenderRequestVersion = 0
    private var senderToReceiverRequestVersion = 0

    // Avoid calling Routes API on every five-second location update.
    private var lastCurrentRouteOrigin: LatLng? = null
    private var lastCurrentRouteRequestTime = 0L

    // Cancel route requests when this Activity is destroyed.
    private val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Read the existing Google Maps API key from AndroidManifest.xml metadata.
    private var routesApiKey: String = ""

    private enum class RouteSegment {
        CURRENT_TO_SENDER,
        SENDER_TO_RECEIVER
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_track)

        val packageModel = intent.getSerializableExtra("package") as? Instant_model

        tv_payment_type = findViewById(R.id.tv_payment_type)
        tv_total_amt = findViewById(R.id.tv_total_amt)
        tv_resiver_name = findViewById(R.id.tv_resiver_name)
        tv_resiver_address = findViewById(R.id.tv_resiver_address)
        img_resiver_phone = findViewById(R.id.img_resiver_phone)
        tv_sender_name = findViewById(R.id.tv_sender_name)
        tv_sender_address = findViewById(R.id.tv_sender_address)
        img_sender_phone = findViewById(R.id.img_sender_phone)
        tv_package_type = findViewById(R.id.tv_package_type)
        tv_frangible = findViewById(R.id.tv_frangible)
        btn_start_navigation = findViewById(R.id.btn_start_navigation)
        tv_km = findViewById(R.id.tv_km)
        tv_package_waight = findViewById(R.id.tv_package_waight)
        btn_delivered = findViewById(R.id.btn_delivered)
        sender_lat_lng = findViewById(R.id.sender_lat_lng)
        receiver_lat_lng = findViewById(R.id.receiver_lat_lng)
        ed_otp = findViewById(R.id.ed_otp)

        senderLatLng = LatLng(
            packageModel?.sender_lat ?: 0.0,
            packageModel?.sender_lang ?: 0.0
        )
        receiverLatLng = LatLng(
            packageModel?.rec_lat ?: 0.0,
            packageModel?.rec_lang ?: 0.0
        )

        tv_payment_type.text = packageModel?.payment_mode
        tv_total_amt.text = packageModel?.total_amt
        tv_resiver_name.text = packageModel?.rec_name
        tv_resiver_address.text = packageModel?.rec_add
        tv_sender_name.text = packageModel?.sender_name
        tv_sender_address.text = packageModel?.sender_add
        tv_package_waight.text = packageModel?.package_size

        // Reuse the API key already declared in AndroidManifest.xml.
        routesApiKey = readGoogleApiKeyFromManifest()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT
        enableEdgeToEdge()

        img_sender_phone.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${packageModel?.sender_phn.orEmpty()}")
            }
            startActivity(dialIntent)
        }

        img_resiver_phone.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${packageModel?.rec_phn.orEmpty()}")
            }
            startActivity(dialIntent)
        }

        btn_delivered.setOnClickListener {
            val enteredOTP = ed_otp.text.toString().trim()

            if (enteredOTP.isEmpty()) {
                Toasty.warning(this, "Please enter OTP", Toasty.LENGTH_SHORT).show()
            } else if (enteredOTP == packageModel?.verify_otp) {
                packageModel.let {
                    if (it != null) {
                        val packageApi = Package_Api()
                        packageApi.DeliverPackage(this, it.id)
                        Toasty.success(
                            this,
                            "Package has been delivered",
                            Toasty.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                }
            } else {
                Toasty.error(this, "Entered OTP is incorrect", Toasty.LENGTH_SHORT).show()
            }
        }

        btn_start_navigation.setOnClickListener {
            startNavigation()
        }

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initLocationUpdates()
        initMap()
    }

    private fun initMap() {
        mapView.getMapAsync { map ->
            googleMap = map
            configureMapInfoWindows()
            riderDirectionIcon = createMarkerIcon(R.drawable.ic_rider_direction)

            addSenderMarker()
            addReceiverMarker()
            drawInitialRoute()
            enableMapLocationFeatures()

            // A location update can arrive before the map is ready. Draw it now if available.
            latestLocation?.let { updateCurrentLocation(it) }
        }
    }

    private fun enableMapLocationFeatures() {
        if (!hasLocationPermission()) {
            requestLocationPermission()
            return
        }

        try {
            googleMap.isMyLocationEnabled = true
        } catch (securityException: SecurityException) {
            Log.e(TAG, "Location permission was not available for the map", securityException)
            return
        }

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
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    latestLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    if (isValidCoordinate(currentLatLng)) {
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f),
                            1000,
                            null
                        )
                        updateCurrentLocation(location)
                    }
                }
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "Unable to retrieve last known location", error)
            }
    }

    private fun startNavigation() {
        if (!hasLocationPermission()) {
            requestLocationPermission()
            return
        }

        if (!::googleMap.isInitialized) {
            Toasty.warning(this, "Map is still loading. Please try again.", Toasty.LENGTH_SHORT).show()
            return
        }

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
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                val currentLocation = location ?: latestLocation
                if (currentLocation == null) {
                    Toasty.warning(
                        this,
                        "Current location is not available yet.",
                        Toasty.LENGTH_SHORT
                    ).show()
                    return@addOnSuccessListener
                }

                val currentLatLng = LatLng(
                    currentLocation.latitude,
                    currentLocation.longitude
                )

                if (!isValidCoordinate(senderLatLng) || !isValidCoordinate(receiverLatLng)) {
                    Toasty.error(
                        this,
                        "Sender or receiver coordinates are missing.",
                        Toasty.LENGTH_SHORT
                    ).show()
                    return@addOnSuccessListener
                }

                drawRoute(
                    currentLatLng,
                    senderLatLng,
                    RouteSegment.CURRENT_TO_SENDER
                )
                drawRoute(
                    senderLatLng,
                    receiverLatLng,
                    RouteSegment.SENDER_TO_RECEIVER
                )

                // This preserves your existing straight-line distance calculation.
                val distanceToSender = calculateDistance(currentLatLng, senderLatLng)
                val distanceToReceiver = calculateDistance(senderLatLng, receiverLatLng)
                val totalDistance = distanceToSender + distanceToReceiver
                tv_km.text = String.format(Locale.US, "%.1fkm", totalDistance)
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "Unable to get current location for navigation", error)
                Toasty.error(this, "Unable to get current location.", Toasty.LENGTH_SHORT).show()
            }
    }

    private fun calculateDistance(origin: LatLng, destination: LatLng): Double {
        val results = FloatArray(1)
        Location.distanceBetween(
            origin.latitude,
            origin.longitude,
            destination.latitude,
            destination.longitude,
            results
        )
        return results[0].toDouble() / 1000.0
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
        if (!hasLocationPermission()) return

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

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        if (::fusedLocationClient.isInitialized && ::locationCallback.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun updateCurrentLocation(location: Location) {
        val previousLocation = latestLocation
        latestLocation = location

        if (!::googleMap.isInitialized) return

        val latLng = LatLng(location.latitude, location.longitude)
        if (!isValidCoordinate(latLng)) {
            Log.w(TAG, "Ignoring invalid current-location coordinates: $latLng")
            return
        }

        val heading = resolveRiderHeading(location, previousLocation)

        if (currentLocationMarker == null) {
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title("Delivery partner")
                .snippet("Live rider location")
                .anchor(0.5f, 0.5f)
                .flat(true)
                .rotation(heading)
                .zIndex(3f)

            markerOptions.icon(
                riderDirectionIcon ?: BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_AZURE
                )
            )

            currentLocationMarker = googleMap.addMarker(markerOptions)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } else {
            currentLocationMarker?.position = latLng
            currentLocationMarker?.rotation = heading
        }

        maybeRefreshCurrentToSenderRoute(latLng)
    }

    /**
     * Returns the rider's direction of travel. Fused Location bearing is preferred;
     * if it is unavailable, the bearing between consecutive GPS fixes is used.
     */
    private fun resolveRiderHeading(
        currentLocation: Location,
        previousLocation: Location?
    ): Float {
        val movementMeters = previousLocation?.distanceTo(currentLocation) ?: 0f

        val heading = when {
            currentLocation.hasBearing() && currentLocation.speed > 0.5f ->
                currentLocation.bearing

            previousLocation != null && movementMeters >= 3f ->
                previousLocation.bearingTo(currentLocation)

            else -> lastRiderHeadingDegrees
        }

        lastRiderHeadingDegrees = ((heading % 360f) + 360f) % 360f
        return lastRiderHeadingDegrees
    }

    private fun maybeRefreshCurrentToSenderRoute(currentLatLng: LatLng) {
        if (!isValidCoordinate(senderLatLng)) return

        val now = SystemClock.elapsedRealtime()
        val previousOrigin = lastCurrentRouteOrigin
        val elapsed = now - lastCurrentRouteRequestTime

        val shouldRefresh = when {
            previousOrigin == null -> true
            elapsed >= CURRENT_ROUTE_MAX_REFRESH_MS -> true
            elapsed >= CURRENT_ROUTE_MIN_REFRESH_MS &&
                    calculateDistance(previousOrigin, currentLatLng) * 1000.0 >=
                    CURRENT_ROUTE_MIN_MOVEMENT_METERS -> true
            else -> false
        }

        if (shouldRefresh) {
            drawRoute(
                currentLatLng,
                senderLatLng,
                RouteSegment.CURRENT_TO_SENDER
            )
        }
    }

    private fun drawRoute(
        origin: LatLng,
        destination: LatLng,
        segment: RouteSegment
    ) {
        if (!::googleMap.isInitialized) return

        if (!isValidCoordinate(origin) || !isValidCoordinate(destination)) {
            Log.e(TAG, "Route skipped because origin or destination coordinates are invalid")
            return
        }

        val requestVersion = when (segment) {
            RouteSegment.CURRENT_TO_SENDER -> {
                lastCurrentRouteOrigin = origin
                lastCurrentRouteRequestTime = SystemClock.elapsedRealtime()
                ++currentToSenderRequestVersion
            }

            RouteSegment.SENDER_TO_RECEIVER -> ++senderToReceiverRequestVersion
        }

        activityScope.launch {
            val decodedPoints = withContext(Dispatchers.IO) {
                fetchRoutePolyline(origin, destination)
            }

            if (isFinishing || isDestroyed || !::googleMap.isInitialized) return@launch

            val isLatestRequest = when (segment) {
                RouteSegment.CURRENT_TO_SENDER ->
                    requestVersion == currentToSenderRequestVersion

                RouteSegment.SENDER_TO_RECEIVER ->
                    requestVersion == senderToReceiverRequestVersion
            }

            if (!isLatestRequest) return@launch

            if (decodedPoints.isNullOrEmpty()) {
                Log.e(TAG, "Routes API returned no polyline for $segment")
                return@launch
            }

            val newPolyline = googleMap.addPolyline(
                PolylineOptions()
                    .addAll(decodedPoints)
                    .color(ContextCompat.getColor(this@Map_track_Activity, R.color.bl))
                    .width(10f)
                    .geodesic(true)
            )

            // Replace only the matching segment, leaving the other route visible.
            when (segment) {
                RouteSegment.CURRENT_TO_SENDER -> {
                    currentToSenderPolyline?.remove()
                    currentToSenderPolyline = newPolyline
                }

                RouteSegment.SENDER_TO_RECEIVER -> {
                    senderToReceiverPolyline?.remove()
                    senderToReceiverPolyline = newPolyline
                }
            }
        }
    }

    /**
     * Reads the API key from the existing AndroidManifest.xml metadata entry:
     * <meta-data android:name="com.google.android.geo.API_KEY" ... />
     */
    private fun readGoogleApiKeyFromManifest(): String {
        return try {
            val appInfo = packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
            val key = appInfo.metaData
                ?.getString("com.google.android.geo.API_KEY")
                ?.trim()
                .orEmpty()

            if (key.isBlank()) {
                Log.e(
                    TAG,
                    "Google API key not found. Check com.google.android.geo.API_KEY in AndroidManifest.xml"
                )
            }
            key
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to read Google API key from AndroidManifest.xml", exception)
            ""
        }
    }

    /** Calls Routes API and returns the decoded overview polyline. Runs on Dispatchers.IO. */
    private fun fetchRoutePolyline(
        origin: LatLng,
        destination: LatLng
    ): List<LatLng>? {
        var connection: HttpURLConnection? = null

        return try {
            if (routesApiKey.isBlank() || routesApiKey.contains("PASTE_")) {
                Log.e(
                    TAG,
                    "Routes API key is missing. Check com.google.android.geo.API_KEY in AndroidManifest.xml."
                )
                return null
            }

            val androidCertSha1 = getSigningCertificateSha1()
            if (androidCertSha1.isNullOrBlank()) {
                Log.e(TAG, "Unable to calculate the app signing certificate SHA-1")
                return null
            }

            val requestBody = JSONObject().apply {
                put("origin", createWaypoint(origin))
                put("destination", createWaypoint(destination))
                put("travelMode", "DRIVE")
                put("polylineQuality", "OVERVIEW")
                put("languageCode", "en")
                put("units", "METRIC")
                put("computeAlternativeRoutes", false)
            }

            connection = (URL(ROUTES_API_URL).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 15_000
                readTimeout = 15_000
                doOutput = true
                useCaches = false
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                setRequestProperty("X-Goog-Api-Key", routesApiKey)
                setRequestProperty("X-Goog-FieldMask", ROUTES_FIELD_MASK)

                // These headers are needed when the Routes API key has Android app restrictions.
                setRequestProperty("X-Android-Package", packageName)
                setRequestProperty("X-Android-Cert", androidCertSha1)
            }

            connection.outputStream.use { output ->
                output.write(requestBody.toString().toByteArray(Charsets.UTF_8))
            }

            val statusCode = connection.responseCode
            val responseStream = if (statusCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }
            val responseText = responseStream
                ?.bufferedReader(Charsets.UTF_8)
                ?.use { it.readText() }
                .orEmpty()

            if (statusCode !in 200..299) {
                Log.e(TAG, "Routes API HTTP $statusCode: $responseText")
                return null
            }

            val responseJson = JSONObject(responseText)
            val routes = responseJson.optJSONArray("routes")
            if (routes == null || routes.length() == 0) {
                Log.e(TAG, "Routes API returned an empty routes array: $responseText")
                return null
            }

            val encodedPolyline = routes
                .optJSONObject(0)
                ?.optJSONObject("polyline")
                ?.optString("encodedPolyline")
                .orEmpty()

            if (encodedPolyline.isBlank()) {
                Log.e(TAG, "Routes API response did not contain routes.polyline.encodedPolyline")
                return null
            }

            decodePolyline(encodedPolyline)
        } catch (exception: Exception) {
            Log.e(TAG, "Routes API request failed: ${exception.message}", exception)
            null
        } finally {
            connection?.disconnect()
        }
    }

    private fun createWaypoint(point: LatLng): JSONObject {
        return JSONObject().put(
            "location",
            JSONObject().put(
                "latLng",
                JSONObject()
                    .put("latitude", point.latitude)
                    .put("longitude", point.longitude)
            )
        )
    }

    /** Decodes Google's encoded polyline format into map coordinates. */
    private fun decodePolyline(encoded: String): List<LatLng> {
        val points = ArrayList<LatLng>()
        var index = 0
        var latitude = 0
        var longitude = 0

        try {
            while (index < encoded.length) {
                var result = 0
                var shift = 0
                var byteValue: Int

                do {
                    if (index >= encoded.length) return emptyList()
                    byteValue = encoded[index++].toInt() - 63
                    if (byteValue < 0) return emptyList()
                    result = result or ((byteValue and 0x1f) shl shift)
                    shift += 5
                } while (byteValue >= 0x20)

                val latitudeDelta = if ((result and 1) != 0) {
                    (result shr 1).inv()
                } else {
                    result shr 1
                }
                latitude += latitudeDelta

                result = 0
                shift = 0

                do {
                    if (index >= encoded.length) return emptyList()
                    byteValue = encoded[index++].toInt() - 63
                    if (byteValue < 0) return emptyList()
                    result = result or ((byteValue and 0x1f) shl shift)
                    shift += 5
                } while (byteValue >= 0x20)

                val longitudeDelta = if ((result and 1) != 0) {
                    (result shr 1).inv()
                } else {
                    result shr 1
                }
                longitude += longitudeDelta

                points.add(
                    LatLng(
                        latitude / 100000.0,
                        longitude / 100000.0
                    )
                )
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to decode route polyline", exception)
            return emptyList()
        }

        return points
    }

    private fun drawInitialRoute() {
        if (isValidCoordinate(senderLatLng) && isValidCoordinate(receiverLatLng)) {
            drawRoute(
                senderLatLng,
                receiverLatLng,
                RouteSegment.SENDER_TO_RECEIVER
            )
        } else {
            Log.e(TAG, "Sender/receiver coordinates are missing or invalid")
        }
    }

    private fun isValidCoordinate(point: LatLng): Boolean {
        return point.latitude.isFinite() &&
                point.longitude.isFinite() &&
                point.latitude in -90.0..90.0 &&
                point.longitude in -180.0..180.0 &&
                !(point.latitude == 0.0 && point.longitude == 0.0)
    }

    private fun hasLocationPermission(): Boolean {
        val finePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarsePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return finePermission || coarsePermission
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
                if (::googleMap.isInitialized) {
                    enableMapLocationFeatures()
                }
                startLocationUpdates()
            } else {
                Toasty.warning(
                    this,
                    "Location permission is required to show your current position.",
                    Toasty.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun addSenderMarker() {
        if (!isValidCoordinate(senderLatLng)) {
            Log.e(TAG, "Sender marker skipped: invalid sender coordinates $senderLatLng")
            return
        }

        senderMarker?.remove()

        val senderName = tv_sender_name.text?.toString()?.trim().orEmpty()
        val senderAddress = tv_sender_address.text?.toString()?.trim().orEmpty()
        val title = if (senderName.isNotBlank()) "PICKUP  ·  $senderName" else "SENDER PICKUP"
        val snippet = senderAddress.ifBlank { "Sender address not available" }

        val markerOptions = MarkerOptions()
            .position(senderLatLng)
            .title(title)
            .snippet(snippet)
            // The marker tip is placed exactly on the sender coordinate.
            .anchor(0.5f, 1.0f)
            .zIndex(2f)

        markerOptions.icon(
            createMarkerIcon(R.drawable.ic_sender_pin)
                ?: BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        )

        senderMarker = googleMap.addMarker(markerOptions)
    }

    private fun addReceiverMarker() {
        if (!isValidCoordinate(receiverLatLng)) {
            Log.e(TAG, "Receiver marker skipped: invalid receiver coordinates $receiverLatLng")
            return
        }

        receiverMarker?.remove()

        val receiverName = tv_resiver_name.text?.toString()?.trim().orEmpty()
        val receiverAddress = tv_resiver_address.text?.toString()?.trim().orEmpty()
        val title = if (receiverName.isNotBlank()) "DELIVERY  ·  $receiverName" else "RECEIVER DELIVERY"
        val snippet = receiverAddress.ifBlank { "Receiver address not available" }

        val markerOptions = MarkerOptions()
            .position(receiverLatLng)
            .title(title)
            .snippet(snippet)
            // The marker tip is placed exactly on the receiver coordinate.
            .anchor(0.5f, 1.0f)
            .zIndex(2f)

        markerOptions.icon(
            createMarkerIcon(R.drawable.ic_receiver_pin)
                ?: BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        )

        receiverMarker = googleMap.addMarker(markerOptions)
    }

    /**
     * Android maps do not have desktop-style hover on touchscreens. Tapping a
     * sender/receiver marker opens this address card as its info window.
     */
    private fun configureMapInfoWindows() {
        googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                val view = layoutInflater.inflate(R.layout.map_marker_info_window, null)
                val titleView = view.findViewById<TextView>(R.id.map_info_title)
                val addressView = view.findViewById<TextView>(R.id.map_info_address)
                val accentView = view.findViewById<View>(R.id.map_info_accent)

                titleView.text = marker.title.orEmpty()
                addressView.text = marker.snippet.orEmpty()
                addressView.visibility = if (marker.snippet.isNullOrBlank()) View.GONE else View.VISIBLE

                val accentColor = when {
                    marker.title.orEmpty().contains("PICKUP", ignoreCase = true) -> Color.parseColor("#07845F")
                    marker.title.orEmpty().contains("DELIVERY", ignoreCase = true) -> Color.parseColor("#3157C8")
                    else -> Color.parseColor("#1684D8")
                }
                accentView.backgroundTintList = ColorStateList.valueOf(accentColor)

                return view
            }

            override fun getInfoContents(marker: Marker): View? = null
        })

        googleMap.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()
            false
        }
    }

    /**
     * Converts an Android vector drawable into a bitmap descriptor.
     * Google Maps marker icons require a bitmap, so this also supports vector assets.
     */
    private fun createMarkerIcon(drawableResId: Int): com.google.android.gms.maps.model.BitmapDescriptor? {
        return try {
            val drawable = ContextCompat.getDrawable(this, drawableResId) ?: return null
            val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 48
            val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 56

            val bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, width, height)
            drawable.draw(canvas)

            BitmapDescriptorFactory.fromBitmap(bitmap)
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to create custom map marker icon", exception)
            null
        }
    }

    private fun getSigningCertificateSha1(): String? {
        return try {
            val certificateBytes: ByteArray? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                packageInfo.signingInfo
                    ?.apkContentsSigners
                    ?.firstOrNull()
                    ?.toByteArray()
            } else {
                @Suppress("DEPRECATION")
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                )
                @Suppress("DEPRECATION")
                packageInfo.signatures?.firstOrNull()?.toByteArray()
            }

            if (certificateBytes == null) return null

            MessageDigest.getInstance("SHA-1")
                .digest(certificateBytes)
                .joinToString(separator = "") { byte ->
                    "%02X".format(Locale.US, byte.toInt() and 0xFF)
                }
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to read app signing certificate", exception)
            null
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
        activityScope.cancel()
        stopLocationUpdates()
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
