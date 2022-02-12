package com.example.chap17

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment!!
        )

        fragment.arguments = args
        fragment.setTargetFragment(caller, 0)
        supportFragmentManager.beginTransaction()
            .replace(R.id.setting_content, fragment)
            .addToBackStack(null)
            .commit()

        return true
    }

}