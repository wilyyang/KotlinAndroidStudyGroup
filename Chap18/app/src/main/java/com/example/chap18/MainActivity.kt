package com.example.chap18

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chap18.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var manager: TelephonyManager

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()
    }

    private val PERMISSIONS_REQUEST_CODE = 100
    private var REQUIRED_PERMISSIONS = arrayOf( Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.ACCESS_NETWORK_STATE)
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
            phoneInfo()
        }
    }

    private fun phoneInfo(){
        val phoneStateListener = object : PhoneStateListener(){
            override fun onServiceStateChanged(serviceState: ServiceState?) {
                when(serviceState?.state){
                    ServiceState.STATE_EMERGENCY_ONLY -> Log.d("wily", "EMERGENCY_ONLY...")
                    ServiceState.STATE_OUT_OF_SERVICE -> Log.d("wily", "OUT_OF_SERVICE...")
                    ServiceState.STATE_POWER_OFF -> Log.d("wily", "POWER_OFF...")
                    ServiceState.STATE_IN_SERVICE -> Log.d("wily", "IN_SERVICE...")
                }
            }

            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                when(state){
                    TelephonyManager.CALL_STATE_IDLE -> Log.d("wily", "IDLE...")
                    TelephonyManager.CALL_STATE_RINGING -> Log.d("wily", "RINGING..$phoneNumber")
                    TelephonyManager.CALL_STATE_OFFHOOK -> Log.d("wily", "OFFHOOK..$phoneNumber")
                }
            }
        }

        manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_SERVICE_STATE or PhoneStateListener.LISTEN_CALL_STATE)

        val countryIso = manager.networkCountryIso
        val operatorName = manager.networkOperatorName

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED){
            val phoneNumber = manager.line1Number
            Log.d("wily", ">> $countryIso, $operatorName, $phoneNumber ${isNetworkAvailable()}")
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.d("wily", "wifi available")
                    true
                }
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.d("wily", "cellular available")
                    true
                }
                else -> false
            }
        }else{
            return connectivityManager.activeNetworkInfo?.isConnected?: false
        }
    }
}