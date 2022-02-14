package com.example.chap17

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class MySettingFragment : PreferenceFragmentCompat(),
SharedPreferences.OnSharedPreferenceChangeListener{
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val idPreference: EditTextPreference? = findPreference("et_id")
        idPreference?.isVisible = true

        idPreference?.summary = "code summary"
        idPreference?.title = "code title"

        idPreference?.summaryProvider = Preference.SummaryProvider <EditTextPreference>{
            preference ->
            val text = preference.text
            if(TextUtils.isEmpty(text)){
                "설정이 되지 않았습니다."
            }else{
                "설정된 ID 값은 : $text 입니다."
            }
        }
        idPreference?.setOnPreferenceClickListener {
            Toast.makeText(context, "고고고", Toast.LENGTH_SHORT).show()
            true
        }

        idPreference?.setOnPreferenceChangeListener { preference, newValue ->
            Log.d("wily", "preference key : ${preference.key}, newValue : $newValue")
            true
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key=="et_id"){
            Log.i("wily", "onSharedPreferenceChanged newValue : "+sharedPreferences?.getString("et_id", ""))
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }
}