package com.example.chap13

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.chap13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var launcher: ActivityResultLauncher<Intent>
    var intentData:String? = null

    lateinit var manager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if(controller != null){
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == RESULT_OK)
            {
                intentData = it.data?.getStringExtra("resultData")
                binding.tvMain.text = intentData
            }
        }

        with(binding){
            val packageInfo = packageManager.getPackageInfo("com.google.android.apps.maps", 0)
            val versionName = packageInfo.versionName
            tvMain.text = "$versionName"

            btnMain.setOnClickListener {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("data1", "hello")
                intent.putExtra("data2", 10)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                launcher.launch(intent)
//                !deprecated
//                startActivityForResult(intent, 10)

            }

            btnShow.setOnClickListener {
                edText.requestFocus()
                manager.showSoftInput(edText, InputMethodManager.SHOW_IMPLICIT)
            }

            btnHide.setOnClickListener {
                manager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            }
        }
    }

//    !deprecated
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
//            val result = data?.getStringExtra("resultData")
//            binding.tvMain.text = result
//        }
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val data1 = savedInstanceState.getString("data1")
        val data2 = savedInstanceState.getInt("data2")
        Toast.makeText(this@MainActivity, "$data1 $data2", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("data1", intentData)
        outState.putInt("data2", 10)
    }
}