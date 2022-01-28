package com.example.ch15_outer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import com.example.ch15_outer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun outerTest(){
        val messengerConnection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                var messenger = Messenger(p1)
                messenger!!.send(
                    Message().apply {
                        what = 20
                        obj = Bundle().apply{
                            putString("data1", "Kokun")
                            putInt("data2", 10)}
                    })
            }
            override fun onServiceDisconnected(p0: ComponentName?) {}
        }

        val intent = Intent("ACTION_OUTER_SERVICE")
        intent.setPackage("com.example.chap15")
        bindService(intent, messengerConnection, Context.BIND_AUTO_CREATE)

        val broadIntent = Intent("ACTION_RECEIVER")
        sendBroadcast(broadIntent)
    }
}