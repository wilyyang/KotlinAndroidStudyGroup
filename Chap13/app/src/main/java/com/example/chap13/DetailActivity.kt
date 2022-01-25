package com.example.chap13

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chap13.databinding.ActivityDetailBinding
import com.example.chap13.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlin.system.measureTimeMillis

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            val data1 = intent.getStringExtra("data1")
            val data2 = intent.getIntExtra("data2", 0)
            tvDetail.text = "$data1 $data2"

            btnDetail.setOnClickListener {
                /*try{
                    val intent = Intent("ACTION_HELLO")
                    val intent = Intent("ACTION_EDIT")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.7749,127.4194"))
                    intent.setPackage("com.google.android.apps.maps")
                    startActivity(intent)
                }catch(e: Exception){
                    Toast.makeText(this@DetailActivity, "no app ...", Toast.LENGTH_SHORT).show()
                }*/

                intent.putExtra("resultData", "world")
                setResult(RESULT_OK, intent)
                finish()
            }

            btnSelf.setOnClickListener {
                val intent = Intent(this@DetailActivity, DetailActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }

            btnTimer.setOnClickListener {
                val channel = Channel<Int>()
                CoroutineScope(Dispatchers.Default + Job()).launch {
                    for(i in 1..2_000_000){
                        channel.send(i)
                    }
                }
                GlobalScope.launch(Dispatchers.Main){
                    channel.consumeEach {
                        tvTimer.text = "sum : $it"
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Toast.makeText(this@DetailActivity, "onNewIntent", Toast.LENGTH_SHORT).show()
    }
}