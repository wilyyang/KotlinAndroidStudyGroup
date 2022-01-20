package com.example.chap11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.chap11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.button2.setOnClickListener {
            val intent = Intent(this, TwoActivity::class.java)
            startActivity(intent)
        }

        binding.button3.setOnClickListener {
            val intent = Intent(this, ThreeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("wily", "Main onSupportNavigateUp")
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuItem1: MenuItem? = menu?.add(0,0,0,"menu1")
        val menuItem2: MenuItem? = menu?.add(0,1,0,"menu2")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        0 -> {
            Log.d("wily", "Option menu 1 selected")
            true
        }
        1 -> {
            Log.d("wily", "Option menu 2 selected")
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}