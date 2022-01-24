package com.example.chap12.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chap12.R
import com.example.chap12.databinding.Fragment1Binding
import com.example.chap12.databinding.Fragment2Binding

class Fragment2 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = Fragment2Binding.inflate(inflater, container, false)
        binding.root.setBackgroundColor(Color.CYAN)
        return binding.root
    }
}