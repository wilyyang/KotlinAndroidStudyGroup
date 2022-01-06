package com.example.chap07

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chap07.databinding.LayoutFrameBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LayoutFrameBinding.inflate(layoutInflater)
        binding.button.setOnClickListener{
            binding.button.visibility = View.INVISIBLE
            binding.image.visibility = View.VISIBLE
        }

        binding.image.setOnClickListener{
            binding.button.visibility = View.VISIBLE
            binding.image.visibility = View.INVISIBLE
        }
        setContentView(binding.root)
    }
}