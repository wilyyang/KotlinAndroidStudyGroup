package com.example.chap11.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chap11.TwoActivity
import com.example.chap11.databinding.FragmentOneBinding

class OneFragment : Fragment() {
    lateinit var binding: FragmentOneBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Wily2", ">Frag onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Wily2", ">Frag onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneBinding.inflate(inflater, container, false)

        binding.button5.setOnClickListener {
            val intent = Intent(context, TwoActivity::class.java)
            startActivity(intent)
        }
        Log.d("Wily2", ">Frag onCreateView")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("Wily2", ">Frag onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Wily2", ">Frag onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Wily2", ">Frag onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Wily2", ">Frag onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Wily2", ">Frag onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Wily2", ">Frag onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Wily2", ">Frag onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("Wily2", ">Frag onDetach")
    }
}