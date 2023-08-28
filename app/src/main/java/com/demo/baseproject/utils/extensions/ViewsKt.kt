package com.demo.baseproject.utils.extensions

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.demo.baseproject.R
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(
    message: String,
    toastLength: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM
) {
    val toast = Toast.makeText(this, message, toastLength)
    toast.setGravity(gravity, 0, 0)
    toast.show()
}

fun View.showSnackBar(message: String, onActionClick: () -> Unit) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
    snackBar.setAction("RESTART") {
        onActionClick.invoke()
    }
    snackBar.setActionTextColor(context.getColor(R.color.colorPrimary))
    snackBar.show()
}