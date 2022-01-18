package com.example.ch9_resource

import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.example.ch9_resource.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val finePermStatus = ContextCompat.checkSelfPermission(this,"android.permission.ACCESS_FINE_LOCATION")

        if(finePermStatus == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf<String>("android.permission.ACCESS_FINE_LOCATION"), 100)
        }

        binding.button1.setOnClickListener {
            DatePickerDialog(this,
                { p0, p1, p2, p3 -> Log.d("wily", "year : $p1, month : ${p2+1}, dayOfMonth : $p3") }, 2020, 8, 21).show()
        }

        binding.delayTextView.setOnClickListener {
            TimePickerDialog(this,
                { p0, p1, p2 -> Log.d("wily", "time : $p1, minute : ${p2+1}") }
                    , 15, 3, true).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            showToast();
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    @RequiresApi(Build.VERSION_CODES.R)
    fun showToast(){
        val toast = Toast.makeText(this, "종료하려면 한 번 더 누르세요.", Toast.LENGTH_SHORT)

        toast.addCallback(object : Toast.Callback(){
            override fun onToastHidden() {
                super.onToastHidden()
                Log.d("wily", "hidden")
            }

            override fun onToastShown() {
                super.onToastShown()
                Log.d("wily", "shown")
            }
        })
        toast.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}