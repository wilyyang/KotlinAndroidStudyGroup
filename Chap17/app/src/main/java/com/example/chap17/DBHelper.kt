package com.example.chap17

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context): SQLiteOpenHelper(context, "wilydb", null, 2){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table WILY_TB (" +
                "_id integer primary key autoincrement,"+
                "name not null,"+
                "phone)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}