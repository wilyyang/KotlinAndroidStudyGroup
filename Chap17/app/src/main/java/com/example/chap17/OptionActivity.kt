package com.example.chap17

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class OptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        Log.d("wily", ">> ${intent.extras?.get("example_key")}")
    }
}