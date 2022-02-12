package com.example.chap17

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class MySettingFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

}