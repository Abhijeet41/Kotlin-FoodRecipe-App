package com.abhi41.foodrecipe.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

class PrintMessage {

    companion object {

        fun showSnackBarAction(message: String, context: Context, view: View, actionMessage: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction(actionMessage){

            }.show()
        }

    }

}