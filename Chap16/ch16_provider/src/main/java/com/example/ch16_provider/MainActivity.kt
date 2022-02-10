package com.example.ch16_provider

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.ch16_provider.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            gallaryForResult.launch(intent)
        }

        binding.cameraButton.setOnClickListener {
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
                "com.example.ch16_provider.fileprovider", file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraForResult.launch(intent)
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
                        binding.userImageView.setImageBitmap(bitmap)
                    } ?: let{
                        Log.d("wily", "bitmap null")
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }

    private val cameraForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val calRatio = calculateInSampleSize(
                    Uri.fromFile(File(filePath)),
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio
                //result.data?.extras?.get("data") as Bitmap
                val bitmap = BitmapFactory.decodeFile(filePath, option)
                bitmap?.let {
                    binding.userImageView.setImageBitmap(bitmap)
                }
            }
        }
}