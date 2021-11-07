package com.example.ybebrightapp.hidok

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HiDokViewModel: ViewModel() {

    var uriFront = MutableLiveData<Uri?>()
    var uriRight = MutableLiveData<Uri?>()
    var uriLeft = MutableLiveData<Uri?>()

    var type: String = ""

    fun setPathFront(uri: Uri) {
        uriFront.value = uri
    }

    fun setPathRight(uri: Uri) {
        uriRight.value = uri
    }

    fun setPathLeft(uri: Uri) {
        uriLeft.value = uri
    }

    fun setTipe(type: String) {
        this.type = type
    }
}