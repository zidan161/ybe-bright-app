package com.example.ybebrightapp.agent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.data.MainRepository

class AgentViewModel(private val repository: MainRepository): ViewModel() {

    fun getAllMember(): MutableLiveData<MutableList<Agent>?> = repository.getAllMember()

    fun getMember(id: String): MutableLiveData<Agent?> = repository.getMember(id)
}