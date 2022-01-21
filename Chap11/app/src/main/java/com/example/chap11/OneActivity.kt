package com.example.chap11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.chap11.databinding.ActivityOneBinding
import com.example.chap11.fragment.MyFragmentPagerAdapter
import com.example.chap11.view.MyPagerAdapter

class OneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewpager.adapter = MyFragmentPagerAdapter(this)
    }
}