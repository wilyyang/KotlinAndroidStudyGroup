package com.example.chap11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chap11.databinding.ActivityThreeBinding

class ThreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityThreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.appcompatTextView.lineHeight = 50
    }
}