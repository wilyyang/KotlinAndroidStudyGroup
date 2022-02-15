package com.example.chap19

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chap19.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission()
    }

    private val PERMISSIONS_REQUEST_CODE = 100
    private var REQUIRED_PERMISSIONS = arrayOf( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
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
            mapTest()
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

                Log.d("wily", "Last Location : $latitude, $longitude, $accuracy, $time")
            }?: run {
                Log.d("wily", "Not.. data")
            }
        }
    }
}