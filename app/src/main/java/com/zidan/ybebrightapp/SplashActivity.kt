package com.zidan.ybebrightapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zidan.ybebrightapp.login.LoginActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}