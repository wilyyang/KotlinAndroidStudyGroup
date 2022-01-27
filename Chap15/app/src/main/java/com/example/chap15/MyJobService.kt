package com.example.chap15

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.IBinder
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
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d("wily3", "MyJobService......onStopJob()")
        return false
    }
}