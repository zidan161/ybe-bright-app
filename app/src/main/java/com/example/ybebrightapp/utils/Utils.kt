package com.example.ybebrightapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.CustomLoadingDialogBinding
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(
        storageDir,
        "$timeStamp.jpg"
    )
}

@SuppressLint("SimpleDateFormat")
fun getDate(): String {
    return SimpleDateFormat("HHmmss dd/MM/yyyy").format(Date())
}

fun Int.setDecimal(): String {
    val format = DecimalFormat("##.###")
    return format.format(this)
}

fun showAlert(ctx: Context, v: View, cancelable: Boolean): AlertDialog {
    return AlertDialog.Builder(ctx)
        .setView(v)
        .setCancelable(cancelable)
        .create()
}

fun Activity.showLoading(): AlertDialog {
    val v = CustomLoadingDialogBinding.inflate(this.layoutInflater)
    return AlertDialog.Builder(this, R.style.CustomAlertDialog)
        .setView(v.root)
        .setCancelable(false)
        .create()
}

fun Fragment.showLoading(): AlertDialog {
    val v = CustomLoadingDialogBinding.inflate(this.layoutInflater)
    return AlertDialog.Builder(this.requireContext(), R.style.CustomAlertDialog)
        .setView(v.root)
        .setCancelable(false)
        .create()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}