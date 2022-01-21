package com.example.chap11.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chap11.data.Data
import com.example.chap11.databinding.ItemMainBinding

class MyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var datas = mutableListOf<Data>()



    override fun getItemCount(): Int = datas.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("wily", "onBindViewHolder : $position")
        val binding = (holder as MyViewHolder).binding
        binding.itemData.text = datas[position].data
        binding.itemContent.text = datas[position].content

        binding.itemRoot.setOnClickListener {
            Log.d("wily", "item root click : $position")
        }
    }

    inner class MyViewHolder(val binding: ItemMainBinding):RecyclerView.ViewHolder(binding.root)
}