package com.example.chap15

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log

class MyJobService : JobService() {

    override fun onCreate() {
        super.onCreate()
        Log.d("wily3", "MyJobService......onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("wily3", "MyJobService......onDestroy()")
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d("wily3", "MyJobService......onStartJob()")
        val extraData = p0?.extras?.getInt("extra_data")
        Thread(Runnable {
            var sum = 0
            for(i in 1..extraData!!){
                sum += i
                SystemClock.sleep(1000)
            }
            Log.d("wily3", "MyJobService......onStartJob... thread result : $sum")
            jobFinished(p0, false)
        }).start()
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d("wily3", "MyJobService......onStopJob()")
        return false
    }
}