package com.example.chap15

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyBinder : Binder(){
    fun funA(arg: Int){
        Log.d("wily3", "arg : $arg")
    }

    fun funB(arg: Int): Int{
        return arg * arg
    }
}
class MyService : Service() {
    var binder: MyBinder = MyBinder()
    override fun onBind(intent: Intent): IBinder? {
        Log.d("wily3", "Service : onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("wily3", "Service : onUnbind")
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("wily3", "Service : onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("wily3", "Service : onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("wily3", "Service : onDestroy")
    }
}