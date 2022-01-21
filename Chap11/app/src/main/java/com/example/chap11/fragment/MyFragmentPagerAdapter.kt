package com.example.chap11.fragment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentPagerAdapter(activity:FragmentActivity): FragmentStateAdapter(activity) {

    private val fragments: List<Fragment>
    init{
        fragments = listOf(OneFragment(), TwoFragment(), ThreeFragment())
        Log.d("wily", "fragments size : ${fragments.size}")
    }
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}