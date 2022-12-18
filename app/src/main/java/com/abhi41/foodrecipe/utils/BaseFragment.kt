package com.abhi41.foodrecipe.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics

open class BaseFragment: Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseCrashlytics.getInstance().log(
            this.javaClass.simpleName
        )

        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCustomKey("Screen_Name", this.javaClass.simpleName)
    }
}