package com.example.chap18

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.chap18.databinding.ActivityMainBinding
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import okhttp3.Callback
import retrofit2.http.*

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
            glideTest()
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
        val url = "https://api.finance.naver.com/siseJson.naver"
        val stringRequest = StringRequest(
            Request.Method.GET,
            url+"?symbol=005930&requestType=1&startTime=20140817&endTime=20210605&timeframe=week",
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
            url,
            {
                Log.d("wily3", "server data : $it")
            },
            {
                Log.d("wily3", "error........ $it")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return mutableMapOf("symbol" to "005930", "requestType" to "1", "startTime" to "20140817", "endTime" to "20210605", "timeframe" to "week")
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

        val jsonRequest =
            JsonObjectRequest(
                Request.Method.GET,
                "https://mdn.github.io/learning-area/javascript/oojs/json/superheroes.json",
                null,
                Response.Listener { response ->
                    val title = response.getString("squadName")
                    val date = response.getString("homeTown")
                    Log.d("wily4", ">>> $title, $date")
                },
                Response.ErrorListener { error ->
                    Log.d("wily4", ">>> error....$error")
                }
            )
        queue.add(jsonRequest)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            "https://mdn.github.io/learning-area/javascript/oojs/json/superheroes.json",
            null,
            Response.Listener { response ->
                for(i in 0 until response.length()){
                    val jsonObject = response[i] as JSONObject
                    val title = jsonObject.getString("secretBase")
                    val date = jsonObject.getString("formed")
                    Log.d("wily4", ">>>>> $title, $date")
                }
            },
            Response.ErrorListener { error ->
                Log.d("wily4", ">>>>> error....$error")
            }
        )
        queue.add(jsonArrayRequest)
    }

    private val addForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Glide.with(this)
                    .load(result.data!!.data)
                    .into(binding.imageView)
            }
        }

    private fun glideTest(){
        Glide.with(this).load(R.drawable.ic_launcher_background).into(binding.imageView)

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
//        addForResult.launch(intent)

        Glide.with(this)
            .load("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMDAxMDZfMjE5%2FMDAxNTc4MzIxMzk5MTEz.JsE3ufdezCu0dFqSpzFdzYVuFU4h4jjOlklQCjRONo8g.ss_gAFzOddBh_YlnDdnpRIJj3tNzCTsg43iDj6T4ogsg.GIF.chemica777%2FKakaoTalk_20191223_231408804_28.gif&type=sc960_832_gif")
            .override(200,200)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.imageView)

        Glide.with(this)
            .load("https://search.pstatic.net/sunny/?src=https%3A%2F%2Fopenclipart.org%2Fimage%2F800px%2F334762&type=sc960_832")
            .into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.root.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }


            })
    }

    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://reqres/in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun retrofitTest(){
        var networkService: INetworkService = retrofit.create(INetworkService::class.java)
        val call0: Call<UserListModel> = networkService.test0("1")
        call0.enqueue(object: retrofit2.Callback<UserListModel> {
            override fun onResponse(
                call: Call<UserListModel>,
                response: retrofit2.Response<UserListModel>
            ) {
                val userList = response.body()
            }

            override fun onFailure(call: Call<UserListModel>, t: Throwable) {
                call.cancel()
            }
        })

        var callList: MutableList<Call<UserModel>> = mutableListOf()
        callList.add(networkService.test1())
        callList.add(networkService.test2("10", "kkang"))
        callList.add(networkService.test3("age", "kkang"))
        callList.add(networkService.test4(mapOf("one" to "hello", "two" to "world"), "kkang"))
        callList.add(networkService.test5(UserModel(id="1", email="ehdrnr1178@gmail.com", firstName = "gildong", lastName = "hong", avatar = "someurl"), "kkang"))
        callList.add(networkService.test6("gildong 길동", "hong 홍", "kkang"))
        callList.add(networkService.test7(mutableListOf("홍길동", "류현진")))
        callList.add(networkService.test8())
        callList.add(networkService.test9("http://www.google.com", "kkang"))

        for(call in callList){
            call.enqueue(object: retrofit2.Callback<UserModel> {
                override fun onResponse(
                    call: Call<UserModel>,
                    response: retrofit2.Response<UserModel>
                ) {
                    val body = response.body()
                    Log.d("wily5", "firstName : ${body?.firstName} lastName : ${body?.lastName}")
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.d("wily5", "onFailure")
                    call.cancel()
                }
            })
        }
    }
}

interface INetworkService{
    @GET("api/users")
    fun test0(@Query("page") page: String): Call<UserListModel>

    @GET("users/list?sort=desc")
    fun test1(): Call<UserModel>

    @GET("group/{id}/users/{name}")
    fun test2(@Path("id") userId: String, @Path("name") arg2: String): Call<UserModel>

    @GET("group/users")
    fun test3(@Query("sort") arg1:String, @Query("name") arg2:String): Call<UserModel>

    @GET("group/users")
    fun test4(@QueryMap options: Map<String, String>, @Query("name") name: String): Call<UserModel>

    @POST("group/users")
    fun test5(@Body user: UserModel, @Query("name") name: String): Call<UserModel>

    @FormUrlEncoded @POST("user/edit")
    fun test6(@Field("first_name") first: String?, @Field("last_name") last: String?, @Query("name") name: String?): Call<UserModel>

    @FormUrlEncoded @POST("tasks")
    fun test7(@Field("title") titles: List<String>): Call<UserModel>

    @Headers("Cache-Control: max-age=640000") @GET("widget/list")
    fun test8(): Call<UserModel>

    @GET
    fun test9(@Url url: String, @Query("name") name: String): Call<UserModel>
}


data class UserModel(
    var id: String,
    var email: String,
    @SerializedName("first_name")
    var firstName: String,
    var lastName: String,
    var avatar: String
)

data class UserListModel(
    var page: String,
    @SerializedName("per_page")
    var perPage: String,
    var total: String,
    @SerializedName("total_pages")
    var totalPages: String,
    var data: List<UserModel>?
)