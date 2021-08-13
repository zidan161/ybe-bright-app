package com.example.ybebrightapp.agent

import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.Agent
import com.example.ybebrightapp.data.MainRepository

class AgentViewModel(private val repository: MainRepository): ViewModel() {

    fun getAllList(): List<Agent> = repository.getAllList()

    fun getListAgent(poin: Int): List<Agent> = repository.getListAgent(poin)

    fun getListReseller(poin: Int): List<Agent> = repository.getListReseller(poin)
}