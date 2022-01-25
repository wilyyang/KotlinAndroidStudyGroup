package com.example.chap14

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chap14.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var receiver: CustomReceiver
    private val screenReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (intent?.action){
                Intent.ACTION_SCREEN_ON -> Log.d("wily2", "ACTION_SCREEN_ON")
                Intent.ACTION_SCREEN_OFF -> Log.d("wily2", "ACTION_SCREEN_OFF")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiver = CustomReceiver()
        val filter = IntentFilter("ACTION_RECEIVER")
        registerReceiver(receiver, filter)

        val screenFilter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenReceiver, screenFilter)

        binding.btnSend.setOnClickListener {
            val intent = Intent(this@MainActivity, MyReceiver::class.java)
            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            Log.e("wily2", "MainActivity unregisterReceiver1")
            unregisterReceiver(receiver)
            Log.e("wily2", "MainActivity unregisterReceiver2")
            unregisterReceiver(screenReceiver)
            Log.e("wily2", "MainActivity unregisterReceiver3")
        }catch(e:Exception){
            e.printStackTrace()
        }finally {
            Log.e("wily2", "MainActivity unregisterReceiver4")
        }

    }
}