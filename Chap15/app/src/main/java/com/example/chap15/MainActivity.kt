package com.example.chap15

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.example.chap15.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // 2. Bind
    lateinit var binding: ActivityMainBinding
    var serviceBinder: MyBinder? = null

    // 3. Messenger
    lateinit var messenger: Messenger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. startService
//        val intent = Intent(this, MyService::class.java)
//        Log.d("wily3", "Main : startService")
//        startService(intent)
//        Log.d("wily3", "Main : stopService")
//        stopService(intent)


        // 2. Bind
        val bindConnection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                serviceBinder = p1 as MyBinder
                Log.d("wily3", "result : ${serviceBinder!!.funB(5)}")
            }
            override fun onServiceDisconnected(p0: ComponentName?) {}
        }

        val bindIntent = Intent(this, MyService::class.java)
        bindService(bindIntent, bindConnection, Context.BIND_AUTO_CREATE)
        serviceBinder?.funA(456)?:Log.d("wily3", "not bind")

        // 3. Messenger
        val messengerConnection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                messenger = Messenger(p1)
                Log.d("wily3", "result : ${messenger!!.send(Message().apply { what = 10; obj = "hello" })}")
            }
            override fun onServiceDisconnected(p0: ComponentName?) {}
        }

        val messengerIntent = Intent(this, MessengerService::class.java)
        bindService(messengerIntent, messengerConnection, Context.BIND_AUTO_CREATE)

        val receiver = MyReceiver()
        registerReceiver(receiver, IntentFilter("ACTION_RECEIVER"))
        val broadIntent = Intent("ACTION_RECEIVER")
        sendBroadcast(broadIntent)
    }
}