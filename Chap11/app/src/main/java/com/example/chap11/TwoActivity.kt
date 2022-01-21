package com.example.chap11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import com.example.chap11.data.Data
import com.example.chap11.databinding.ActivityTwoBinding
import com.example.chap11.view.MyPagerAdapter
import com.example.chap11.view.MyPagerViewHolder

class TwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =ActivityTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datas = mutableListOf<String>()
        datas.add("hello")
        datas.add("world")
        datas.add("OK")
        binding.viewpager.adapter = MyPagerAdapter(datas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
        })
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("wily", "Two onSupportNavigateUp")
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}