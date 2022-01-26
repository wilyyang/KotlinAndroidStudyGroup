package com.example.chap15

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.chap15.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var serviceBinder: MyBinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val intent = Intent(this, MyService::class.java)
//        Log.d("wily3", "Main : startService")
//        startService(intent)
//        Log.d("wily3", "Main : stopService")
//        stopService(intent)

        val connection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                serviceBinder = p1 as MyBinder
                Log.d("wily3", "result : ${serviceBinder!!.funB(5)}")
            }
            override fun onServiceDisconnected(p0: ComponentName?) {}
        }
        val bindIntent = Intent(this, MyService::class.java)
        bindService(bindIntent, connection, Context.BIND_AUTO_CREATE)
        serviceBinder?.funA(456)?:Log.d("wily3", "not bind")
        Log.d("wily3", "result : ${serviceBinder?.funB(5)}")
//        unbindService(connection)

//        val intentOuter = Intent("ACTION_OUTER_SERVICE")
//        intentOuter.setPackage("com.example.test_outter")
//        startService(intent)
    }
}