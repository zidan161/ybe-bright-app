package com.zidan.ybebrightapp.agent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zidan.ybebrightapp.model.Agent
import com.zidan.ybebrightapp.data.MainRepository

class AgentViewModel(private val repository: MainRepository): ViewModel() {

    fun getAllMember(): MutableLiveData<MutableList<Agent>?> = repository.getAllMember()

    fun getMember(id: String): MutableLiveData<Agent?> = repository.getMember(id)
}