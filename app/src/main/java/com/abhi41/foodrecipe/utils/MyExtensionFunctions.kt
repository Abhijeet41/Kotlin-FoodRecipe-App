package com.abhi41.foodrecipe.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/* we use this extension function because when we run our app first time our requirement is
   It should get the data from api only not from database.
    so when we clear our app data only requestApiData() is called not the readDataBase ()

*/

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}