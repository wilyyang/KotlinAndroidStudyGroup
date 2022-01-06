package com.test.chap05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.test.chap05.databinding.LayoutSampleBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LayoutSampleBinding.inflate(layoutInflater)
        setContentView(binding.root);
        binding.visibleBtn.setOnClickListener{
            binding.targetView.visibility = View.VISIBLE
        }
        binding.invisibleBtn.setOnClickListener{
            binding.targetView.visibility = View.INVISIBLE
        }
    }
}