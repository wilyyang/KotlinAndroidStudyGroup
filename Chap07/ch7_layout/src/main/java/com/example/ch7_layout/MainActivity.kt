package com.example.ch7_layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.CompoundButton
import com.example.ch7_layout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.checkBox.setOnCheckedChangeListener{
            compoundButton, b->
            Log.d("wily", "체크박스 클릭")
        }

        binding.button.setOnClickListener{
            Log.d("kking", "클릭 이벤트")
        }
        binding.button.setOnLongClickListener {
            Log.d("kking", "롱클릭 이벤트")
            true
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                Log.d("wily", "Touch down event x: ${event.x}, rawX: ${event.rawX}")
            }
            MotionEvent.ACTION_UP ->{
                Log.d("wily", "Touch up event")
            }

        }
        return super.onTouchEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KeyEvent.KEYCODE_0 -> Log.d("wily", "0 키를 눌렀네요")
            KeyEvent.KEYCODE_A -> Log.d("wily", "A 키를 눌렀네요")

            KeyEvent.KEYCODE_BACK -> Log.d("wily", "BACK Button을 눌렀네요")
            KeyEvent.KEYCODE_VOLUME_UP -> Log.d("wily", "Volume Up 키를 눌렀네요")
            KeyEvent.KEYCODE_VOLUME_DOWN -> Log.d("wily", "Volume Down 키를 눌렀네요")
        }
        Log.d("wily", "onKeyDown")
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("wily", "onKeyUp")
        return super.onKeyUp(keyCode, event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("wily", "BACK Button을 눌렀네요")
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Log.d("wily", "체크박스 클릭");
    }

    class MyEventHandler : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            Log.d("wily", "체크박스 클릭");
        }

    }
}