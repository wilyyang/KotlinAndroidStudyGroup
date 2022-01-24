package com.example.chap12.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chap12.R
import com.example.chap12.databinding.Fragment1Binding
import com.example.chap12.databinding.Fragment6Binding

class Fragment6 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = Fragment6Binding.inflate(inflater, container, false)
        binding.root.setBackgroundColor(Color.MAGENTA)
        return binding.root
    }
}