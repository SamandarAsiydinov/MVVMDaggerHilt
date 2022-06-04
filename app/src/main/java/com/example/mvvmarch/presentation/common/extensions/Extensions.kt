package com.example.mvvmarch.presentation.common.extensions

import android.app.AlertDialog
import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible

fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun Context.showGenericAlertDialog(message: String) {
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("OK") {d, _ ->
            d.cancel()
        }
    }.show()
}
fun View.visible() {
    isVisible = true
}
fun View.gone() {
    isVisible = false
}