package com.abhi41.foodrecipe.screens.splashScreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.screens.MainActivity
import com.abhi41.foodrecipe.utils.BaseActivity

class SplashScreen : BaseActivity() {
    private var handler = Handler(Looper.myLooper()!!)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler.removeCallbacksAndMessages(null)
        this.finishAffinity()
    }
}