package com.example.chap14

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chap14.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val screenReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action){
                Intent.ACTION_SCREEN_ON -> Log.d("wily2", "ACTION_SCREEN_ON")
                Intent.ACTION_SCREEN_OFF -> Log.d("wily2", "ACTION_SCREEN_OFF")
            }
        }
    }

    private val batteryReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action){
                Intent.ACTION_BATTERY_OKAY -> Log.d("wily2", "ACTION_BATTERY_OKAY")
                Intent.ACTION_BATTERY_CHANGED -> Log.d("wily2", "ACTION_BATTERY_CHANGED")
                Intent.ACTION_BATTERY_LOW -> Log.d("wily2", "ACTION_BATTERY_LOW")
                Intent.ACTION_POWER_CONNECTED -> Log.d("wily2", "ACTION_POWER_CONNECTED")
                Intent.ACTION_POWER_DISCONNECTED -> Log.d("wily2", "ACTION_POWER_DISCONNECTED")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerReceiver(screenReceiver, IntentFilter(Intent.ACTION_SCREEN_ON).apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        })

        registerReceiver(batteryReceiver,
            IntentFilter(Intent.ACTION_BATTERY_OKAY).apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        })

        binding.btnSend.setOnClickListener {
            val intent = Intent(this@MainActivity, MyReceiver::class.java)
            sendBroadcast(intent)
        }

        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, intentFilter)
        val status = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        if(status == BatteryManager.BATTERY_STATUS_CHARGING){
            val chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100
            Log.d("wily2", "batteryPct : $batteryPct")
            when(chargePlug){
                BatteryManager.BATTERY_PLUGGED_USB -> Log.d("wily2", "BATTERY_PLUGGED_USB")
                BatteryManager.BATTERY_PLUGGED_AC -> Log.d("wily2", "BATTERY_PLUGGED_AC")
                else -> Log.d("wily2", "not charging")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenReceiver)
        unregisterReceiver(batteryReceiver)
    }
}