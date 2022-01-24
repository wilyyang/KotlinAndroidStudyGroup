package com.example.chap12

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chap12.databinding.ActivityMainBinding
import com.example.chap12.databinding.ItemRecyclerviewBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datas = mutableListOf<String>()
        for(i in 1..19)
            datas.add("Item $i")

        val layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = layoutManager
        val adapater = MyAdapter(datas)
        binding.recyclerView.adapter = adapater
        binding.recyclerView.addItemDecoration(MyDecoration(applicationContext))

    }
}

class MyViewHolder(val binding: ItemRecyclerviewBinding):
    RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder =
        MyViewHolder(
            ItemRecyclerviewBinding.inflate(
            LayoutInflater.from(
            parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemData.text = datas[position]
    }
}

class MyDecoration(val context: Context) : RecyclerView.ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view) + 1
        if(index % 3 == 0)
            outRect.set(10,10,10,60)
        else
            outRect.set(10,10,10,0)
        view.setBackgroundColor(Color.parseColor("#28A0FF"))
        ViewCompat.setElevation(view, 20.0f)
    }
}