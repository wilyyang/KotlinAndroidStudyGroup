package com.example.chap17

import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.example.chap17.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileCode()
    }

    fun databaseCode(){
        val writedb = DBHelper(this).writableDatabase
        writedb.execSQL(
            "insert into WILY_TB (name, phone) values (?,?)",
            arrayOf<String>("wily", "0101111")
        )

        writedb.insert("WILY_TB", null, ContentValues().apply {
            put("name", "yang")
            put("phone", "4916-8845")
        })
        writedb.close()

        val readdb = DBHelper(this).readableDatabase
        val cursor1 = readdb.rawQuery("select * from WILY_TB", null)
        while (cursor1.moveToNext()) {
            val id = cursor1.getString(0)
            val name = cursor1.getString(1)
            val phone = cursor1.getString(2)
            Log.e("wily", "id $id name $name phone $phone")
        }


        val cursor2 = readdb.query(
            "WILY_TB", arrayOf<String>("name", "phone"), "phone=?",
            arrayOf<String>("0101111"), null, null, null
        )
        while (cursor2.moveToNext()) {
            val name = cursor2.getString(0)
            val phone = cursor2.getString(1)
            Log.e("wily", ">> name $name phone $phone")
        }

        readdb.close()
    }

    fun fileCode(){

        val file = File(filesDir, "test.txt")
        val writeStream: OutputStreamWriter = file.writer()
        writeStream.write("hello world")
        writeStream.flush()

        val readStream: BufferedReader = file.reader().buffered()
        readStream.forEachLine {
            Log.d("wily", "$it")
        }

        openFileOutput("test.txt", Context.MODE_PRIVATE).use {
            it.write("hello world!!".toByteArray())
        }

        openFileInput("test.txt").bufferedReader().forEachLine {
            Log.d("wily", ">> $it")
        }

        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            Log.d("wily", "ExternalStorageState MOUNTED")
        }else{
            Log.d("wily", "ExternalStorageState UNMOUNTED")
        }

        val fileExternal: File? = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        Log.d("wily", "${fileExternal?.absolutePath}")

    }
}