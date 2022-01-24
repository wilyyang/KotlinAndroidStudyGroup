package com.example.chap12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chap12.databinding.ActivityTabBinding
import com.example.chap12.fragment.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    private val fragments: List<Fragment>
    init{
        fragments = listOf(Fragment1(), Fragment2(), Fragment3(), Fragment4(), Fragment5(), Fragment6())
        Log.d("wily", "fragments size : ${fragments.size}")
    }
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}

class TabActivity : AppCompatActivity() {
    lateinit var binding: ActivityTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        binding = ActivityTabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){

            viewpager.adapter = MyFragmentPagerAdapter(this@TabActivity)
            TabLayoutMediator(tabLayout, viewpager, object:TabLayoutMediator.TabConfigurationStrategy{
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = "Tab${(position+1)}"
                }
            }).attach()

            /*val tab1: TabLayout.Tab = tabLayout.newTab()
            tab1.text = "Tab1"
            tabLayout.addTab(tab1)

            val tab2: TabLayout.Tab = tabLayout.newTab()
            tab2.text = "Tab2"
            tabLayout.addTab(tab2)

            val tab3: TabLayout.Tab = tabLayout.newTab()
            tab3.text = "Tab3"
            tabLayout.addTab(tab3)

            tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val transaction = supportFragmentManager.beginTransaction()
                    when(tab?.text){
                        "Tab1" -> transaction.replace(R.id.tabContent, Fragment1())
                        "Tab2" -> transaction.replace(R.id.tabContent, Fragment2())
                        "Tab3" -> transaction.replace(R.id.tabContent, Fragment3())
                        "Tab4" -> transaction.replace(R.id.tabContent, Fragment4())
                        "Tab5" -> transaction.replace(R.id.tabContent, Fragment5())
                        "Tab6" -> transaction.replace(R.id.tabContent, Fragment6())
                    }
                    transaction.commit()
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    //
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    //
                }
            })*/

            mainDrawerView.setNavigationItemSelectedListener {
                Log.d("wily", "navigation item click... ${it.title}")
                true
            }

            extendedFab.setOnClickListener {
                when(extendedFab.isExtended){
                    true -> extendedFab.shrink()
                    false -> extendedFab.extend()
                }
            }
        }
    }
}