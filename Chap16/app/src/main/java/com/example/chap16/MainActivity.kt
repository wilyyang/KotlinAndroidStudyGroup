package com.example.chap16

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.chap16.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()
    }

    private val PERMISSIONS_REQUEST_CODE = 100
    private var REQUIRED_PERMISSIONS = arrayOf( Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE)
    private fun requestPermission(){
        var permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)){
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }else{
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }
        }else{
            callCall()
        }
    }

    //Contacts
    private fun callContacts() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        contactForResult.launch(intent)
    }

    private val contactForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->

            if(result.resultCode == Activity.RESULT_OK){
                val cursor = contentResolver.query(
                    result.data!!.data!!,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER),
                    null,
                    null,
                    null
                )
                Log.d("wily", "cursor size....${cursor?.count}")
                if(cursor!!.moveToFirst()){
                    val name = cursor?.getString(0)
                    val phone = cursor?.getString(1)

                    val resultStr: String? = result.data?.dataString
                    Log.d("wily", "result : $resultStr name : $name phone : $phone")
                }
            }
        }


    //Gallary
    private fun callGallary() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type="image/*"
        gallaryForResult.launch(intent)
    }

    private val gallaryForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                try{
                    val calRatio = calculateInSampleSize(result.data!!.data!!,
                        resources.getDimensionPixelSize(R.dimen.imgSize),
                        resources.getDimensionPixelSize(R.dimen.imgSize))
                    val option = BitmapFactory.Options()
                    option.inSampleSize = calRatio

                    var inputStream = contentResolver.openInputStream(result.data!!.data!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                    inputStream!!.close()
                    inputStream = null
                    bitmap?.let {
                        binding.gallaryResult.setImageBitmap(bitmap)
                    } ?: let{
                        Log.d("wily", "bitmap null")
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try{
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        }catch(e: Exception){
            e.printStackTrace()
        }

        val (height: Int, width: Int) = options.run{ outHeight to outWidth}
        var inSampleSize = 1

        if(height > reqHeight || width > reqWidth){
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while(halfHeight / inSampleSize >= reqHeight &&
                    halfWidth / inSampleSize >= reqWidth){
                inSampleSize *=2
            }
        }
        return inSampleSize
    }

    //Camera
    lateinit var filePath:String
    private fun callCamera() {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
        filePath = file.absolutePath
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.chap16.fileprovider", file
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        cameraForResult.launch(intent)
    }

    private val cameraForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val option = BitmapFactory.Options()
                option.inSampleSize = 10
                //result.data?.extras?.get("data") as Bitmap
                val bitmap = BitmapFactory.decodeFile(filePath, option)
                bitmap?.let {
                    binding.gallaryResult.setImageBitmap(bitmap)
                }
            }
        }


    //Map
    private fun callMap() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5662952,126.9779451"))
        startActivity(intent)
    }

    //Call
    private fun callCall() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:010-4916-8845"))
        startActivity(intent)
    }
}