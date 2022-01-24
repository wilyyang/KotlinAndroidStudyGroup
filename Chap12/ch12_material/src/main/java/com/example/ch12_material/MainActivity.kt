package com.example.ch12_material

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ch12_material.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    class MyFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
        private val fragments = listOf(OneFragment(), TwoFragment(), ThreeFragment(), OneFragment(), TwoFragment(), ThreeFragment(), OneFragment(), TwoFragment(), ThreeFragment())
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        val adapter = MyFragmentPagerAdapter(this)
        with(binding){
            viewpager.adapter = adapter
            TabLayoutMediator(tabs, viewpager
            ) { tab, position -> tab.text = "Tab${(position + 1)}" }.attach()

            fab.setOnClickListener {
                when(fab.isExtended){
                    true -> fab.shrink()
                    false -> fab.extend()
                }
            }
            mainDrawerView.setNavigationItemSelectedListener {
                Toast.makeText(this@MainActivity, "Select item ${it.title}", Toast.LENGTH_SHORT).show()

                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Search $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        when(item.itemId){
            R.id.menu_setting -> Toast.makeText(this@MainActivity, "menu setting", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}