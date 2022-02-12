package com.example.chap17

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        requestPermission()
    }

    private val PERMISSIONS_REQUEST_CODE = 100
    private var REQUIRED_PERMISSIONS = arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun requestPermission(){
        var permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }else{
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }
        }else{
            prefCode()
        }
    }

    private fun databaseCode(){
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

    private fun fileCode(){

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

        val fileEx = File(getExternalFilesDir(null), "test.txt")
        Log.d("wily", "${fileEx?.absolutePath}")
        val writeStreamEx: OutputStreamWriter = fileEx.writer()
        writeStreamEx.write("Hello wily!")
        writeStreamEx.flush()
        writeStreamEx.close()

        val readStreamEx: BufferedReader = fileEx.reader().buffered()
        readStreamEx.forEachLine {
            Log.d("wily", "$it")
        }

        readStreamEx.close()

        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        cursor?.let {
            while(cursor.moveToNext()){
                Log.d("wily!", "_id : ${cursor.getLong(0)}, name : ${cursor.getString(1)}")
            }
            cursor.moveToFirst()
        }
        val contentUri: Uri = ContentUris.withAppendedId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            cursor!!.getLong(0)
        )

        val resolver = applicationContext.contentResolver
        resolver.openInputStream(contentUri).use { stream ->
            val option = BitmapFactory.Options()
            option.inSampleSize = 10
            val bitmap = BitmapFactory.decodeStream(stream, null, option)
            binding.resultImageView.setImageBitmap(bitmap)
        }
    }

    private fun prefCode(){
        val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().run {
            putString("data1", "hello")
            putInt("data2", 10)
            commit()
        }

        val data1 = sharedPref.getString("data1", "default")
        val data2 = sharedPref.getInt("data2", 10)
        Log.d("wily", "data1 $data1 data2 $data2")

    }
}