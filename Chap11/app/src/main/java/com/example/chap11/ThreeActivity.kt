package com.example.chap11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.chap11.data.Data
import com.example.chap11.databinding.ActivityThreeBinding
import com.example.chap11.view.MyAdapter
import com.example.chap11.view.MyDecoration

class ThreeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThreeBinding
    private lateinit var adapter: MyAdapter

    val mDatas = mutableListOf<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.appcompatTextView.lineHeight = 50

        initializeList()
        initDogRecyclerView()

        binding.button6.setOnClickListener {
            adapter.datas.add(Data("new", "hello"))
            adapter.notifyDataSetChanged()
        }
    }

    fun initializeList(){
        with(mDatas){
            add(Data("dog1", "1okok hello"))
            add(Data("dog2", "2okok hello"))
            add(Data("dog3", "3okok hsdgrsjtyhsdhello"))
            add(Data("dog4", "okok hello"))
            add(Data("dog5", "4okok hello"))
            add(Data("dog6", "5okok helgrsjtyhsdhelgrsjtyhsdhelgrsjtyhsdhelgrsjtyhsdhello"))
            add(Data("dog7", "7okok hello"))
        }
    }

    fun initDogRecyclerView(){
        adapter = MyAdapter()
        adapter.datas = mDatas
        binding.recyclerView.adapter = adapter
        val linearLayoutManaer = LinearLayoutManager(this)
        linearLayoutManaer.orientation = LinearLayoutManager.HORIZONTAL

        val gridLayoutManaer = GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, true)

        val staggeredLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        binding.recyclerView.layoutManager = staggeredLayoutManager
        //binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayout.HORIZONTAL))
        binding.recyclerView.addItemDecoration(MyDecoration(this))
    }
}