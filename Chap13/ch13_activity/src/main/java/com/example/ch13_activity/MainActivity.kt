package com.example.ch13_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch13_activity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    var datas: MutableList<String>? = null
    lateinit var adapter:MyAdapter
    lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == RESULT_OK)
            {
                it.data!!.getStringExtra("result")?.let{
                    datas?.add(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        datas = savedInstanceState?.let {
            it.getStringArrayList("datas")?.toMutableList()
        } ?: let{
            mutableListOf<String>()
        }

        with(binding){
            mainFab.setOnClickListener{
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                launcher.launch(intent)
            }

            val layoutManager = LinearLayoutManager(this@MainActivity)
            mainRecyclerView.layoutManager = layoutManager
            adapter = MyAdapter(datas)
            mainRecyclerView.adapter = adapter
            mainRecyclerView.addItemDecoration(
                DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("datas", ArrayList(datas))
    }
}