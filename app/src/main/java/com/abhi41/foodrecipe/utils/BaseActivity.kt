package com.abhi41.foodrecipe.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseCrashlytics.getInstance().log(
            this.javaClass.simpleName
        )

        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCustomKey("Screen_Name", this.javaClass.simpleName)
    }
}