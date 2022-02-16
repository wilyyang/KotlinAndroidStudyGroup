package com.example.chap19

import android.Manifest
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chap19.databinding.ActivityMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?)!!.getMapAsync(this)
        requestPermission()
    }

    private val PERMISSIONS_REQUEST_CODE = 100
    private var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private fun requestPermission(){

        var permissionCheck = true
        for(permission in REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionCheck = false
            }
        }

        if(!permissionCheck){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
        }else{
            googleMapTest()
        }
    }

    private fun mapTest(){
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager

        var result = "All Providers : "
        val providers = manager.allProviders
        for(provider in providers){
            result += "$provider, "
        }
        Log.d("wily", result)

        result = "Enabled Providers : "
        val enabledProviders = manager.getProviders(true)
        for(provider in enabledProviders){
            result += "$provider, "
        }
        Log.d("wily", result)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            val location: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let{
                val latitude = location.latitude
                val longitude = location.longitude
                val accuracy = location.accuracy
                val time = location.time

                Log.d("wily5", "Last Location : $latitude, $longitude, $accuracy, $time")
            }?: run {
                Log.d("wily5", "Not.. data")
            }

            val listener: LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val accuracy = location.accuracy
                    val time = location.time

                    Log.d("wily5", "Location : $latitude, $longitude, $accuracy, $time")

                }

                override fun onProviderEnabled(provider: String) {
                    super.onProviderEnabled(provider)
                }

                override fun onProviderDisabled(provider: String) {
                    super.onProviderDisabled(provider)
                }
            }

            val provider = manager.getBestProvider(Criteria().apply {
                isSpeedRequired = true
            }, true)
            Log.d("wily5", "result : $provider")
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 1f, listener)
//            manager.removeUpdates(listener)
        }
    }

    private fun mapApiTest(){
        val providerClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        val connectionCallback = object: GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(p0: Bundle?) {
                if(ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                    providerClient.lastLocation.addOnSuccessListener(this@MainActivity
                    ) { location ->
                        val latitude = location?.latitude
                        val longitude = location?.longitude
                        val accuracy = location?.accuracy
                        val time = location?.time

                        Log.d("wily6", "Location : $latitude, $longitude, $accuracy, $time")
                    }
                }
            }

            override fun onConnectionSuspended(p0: Int) {
                Log.d("wily6", "onConnectionSuspended")
            }
        }

        val onConnectionFailedCallback = GoogleApiClient.OnConnectionFailedListener { Log.d("wily6", "onConnectionFailed") }

        val apiClient: GoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(connectionCallback)
            .addOnConnectionFailedListener(onConnectionFailedCallback)
            .build()
        apiClient.connect()
    }

    private fun googleMapTest(){

    }

    var googleMap: GoogleMap? = null
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        val latLng = LatLng(37.566610, 126.978403)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("서울시청")
        markerOptions.snippet("Tel:01-120")
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.frug))

        val marker = googleMap?.addMarker(markerOptions)


        googleMap?.setOnMapClickListener { latLng ->
            Log.d("wily7", "click : ${latLng.latitude}, ${latLng.longitude}")
        }

        googleMap?.setOnMapLongClickListener { latLng ->
            Log.d("wily7", "long click : ${latLng.latitude}, ${latLng.longitude}")
        }

        googleMap?.setOnCameraIdleListener {
            val position = googleMap!!.cameraPosition
            val zoom = position.zoom
            val latitude = position.target.latitude
            val longitude = position.target.longitude
            Log.d("wily7", "user change : $zoom, $latitude, $longitude")
        }

        googleMap?.setOnMarkerClickListener { marker ->
            Log.d("wily7", "OnMarkerClick")
            marker?.showInfoWindow()
            true
        }

        googleMap?.setOnInfoWindowClickListener { marker ->
            Log.d("wily7", "InfoWindowClick")
        }
    }
}