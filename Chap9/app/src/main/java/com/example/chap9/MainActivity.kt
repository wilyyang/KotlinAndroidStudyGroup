package com.example.chap9

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.example.chap9.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = getString(R.string.txt_data2)
        binding.textView.setTextColor(ResourcesCompat.getColor(resources, R.color.txt_color, null))
        binding.textView.setTextSize(resources.getDimension(R.dimen.txt_size))

        binding.imageView.setImageDrawable(ResourcesCompat.getDrawable(resources,
            android.R.drawable.alert_dark_frame, null))
        binding.textView2.text = getString(android.R.string.emptyPhoneNumber)

        val displayMetrics = DisplayMetrics()
        display?.getRealMetrics(displayMetrics)
        binding.printView.text="""
            widthPixels : ${displayMetrics.widthPixels}
            heightPixels : ${displayMetrics.heightPixels}
            density : ${displayMetrics.density}
            densityDpi : ${displayMetrics.densityDpi}
            scaledDensity : ${displayMetrics.scaledDensity}
            xdpi : ${displayMetrics.xdpi}
            ydpi : ${displayMetrics.ydpi}
        """.trimIndent()

    }
}