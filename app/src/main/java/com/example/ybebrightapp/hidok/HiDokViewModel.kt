package com.example.ybebrightapp.hidok

import android.net.Uri
import androidx.lifecycle.ViewModel

class HiDokViewModel: ViewModel() {

    var uriFront: Uri? = null
    var uriRight: Uri? = null
    var uriLeft: Uri? = null

    var type: String = ""

    fun setPathFront(uri: Uri) {
        uriFront = uri
    }

    fun setPathRight(uri: Uri) {
        uriRight = uri
    }

    fun setPathLeft(uri: Uri) {
        uriLeft = uri
    }

    fun setTipe(type: String) {
        this.type = type
    }
}