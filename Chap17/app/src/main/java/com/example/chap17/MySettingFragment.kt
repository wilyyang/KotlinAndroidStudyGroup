package com.example.chap17

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

class MySettingFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val idPreference: EditTextPreference? = findPreference("et_id")
        idPreference?.isVisible = true

        idPreference?.summary = "code summary"
        idPreference?.title = "code title"
    }
}