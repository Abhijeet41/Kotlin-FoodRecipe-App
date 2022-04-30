package com.abhi41.foodrecipe.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class PrintMessage {

    companion object {

        fun showSnackBarAction(message: String, view: View, actionMessage: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction(actionMessage){

            }.show()
        }

        fun showToastMessage(message: String?,context: Context){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }

        fun messageLogD (tag: String,message: String){
            Log.d(tag,message)
        }

    }

}