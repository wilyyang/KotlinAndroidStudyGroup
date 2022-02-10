package com.example.chap17

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chap17.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = openOrCreateDatabase("testdb", Context.MODE_PRIVATE, null)
        db.execSQL("create table USER_TB (" +
        "_id integer primary key autoincrement,"+
        "name not null,"+
        "phone")
        db.execSQL("insert into USER_TB (name, phone) values (?,?)",
        arrayOf<String>("wily", "0101111"))

    }
}