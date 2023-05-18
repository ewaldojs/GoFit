package com.ewaldo.gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    private val preference = "pref"
    private val name = "temp"
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE)
        if (sharedPreferences!!.contains(name)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_splash_screen)
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
            val editor: SharedPreferences.Editor=
                sharedPreferences!!.edit()
            editor.putString(name,"temp")
            editor.apply()
        }
    }
}