package com.zidan.ybebrightapp.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransactionViewModel: ViewModel() {

    private val stock = MutableLiveData(true)

    fun triggerLoad() {
        stock.value = !stock.value!!
    }

    fun getTrigger() = stock
}