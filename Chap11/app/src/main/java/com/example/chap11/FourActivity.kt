package com.example.chap11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.chap11.databinding.ActivityFourBinding
import com.example.chap11.fragment.TwoFragment

class FourActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val fragment2 = TwoFragment()
        transaction.add(R.id.fragmentView2, fragment2)
        transaction.addToBackStack(null)
        transaction.commit()

        Log.d("Wily2", "Act onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Wily2", "Act onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Wily2", "Act onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Wily2", "Act onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Wily2", "Act onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Wily2", "Act onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Wily2", "Act onRestart")
    }
}