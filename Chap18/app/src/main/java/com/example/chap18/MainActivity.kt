package com.example.chap18

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
        Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CHANGE_NETWORK_STATE,
        Manifest.permission.INTERNET)
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
            httpRequest()
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
        val networkReq: NetworkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.requestNetwork(networkReq, object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("wily", "NetworkCallback...onAvailable...")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d("wily", "NetworkCallback...onUnavailable...")
            }
        })

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

    private fun httpRequest(){
        val stringRequest = StringRequest(
            Request.Method.GET,
            "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=apple",
            {
                Log.d("wily", "server data : $it")
            },
            {
                Log.d("wily", "error........ $it")
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)

        val postRequest = object : StringRequest(
            Request.Method.POST,
            "https://search.naver.com/search.naver?sm=tab_hty.top&whare=nexearch",
            {
                Log.d("wily3", "server data : $it")
            },
            {
                Log.d("wily3", "error........ $it")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return mutableMapOf("query" to "apple")
            }
        }
        queue.add(postRequest)

        val imageRequest = ImageRequest(
            "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fopenclipart.org%2Fimage%2F800px%2F334762&type=sc960_832",
            {
                response ->
                binding.imageView.setImageBitmap(response)
            },
            0,0,
            ImageView.ScaleType.CENTER_CROP,
            null,
            {
                Log.d("wily3", "error........ $it")
            }
        )
        queue.add(imageRequest)

        val imgMap = HashMap<String, Bitmap>()
        val imageLoader = ImageLoader(queue, object : ImageLoader.ImageCache{
            override fun getBitmap(url: String?): Bitmap? {
                return imgMap[url]
            }

            override fun putBitmap(url: String, bitmap: Bitmap) {
                imgMap[url] = bitmap
            }
        })
        binding.networkImageView.setImageUrl("https://search.pstatic.net/sunny/?src=https%3A%2F%2Fi.scdn.co%2Fimage%2Fab67616d0000b273a702de976f599b65d4943eee&type=sc960_832", imageLoader)
    }
}