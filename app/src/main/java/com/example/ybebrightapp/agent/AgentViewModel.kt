package com.example.ybebrightapp.agent

import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.data.MainRepository

class AgentViewModel(private val repository: MainRepository): ViewModel() {

    fun getAllList(): List<Agent> = repository.getAllList()

    fun getListAgentTunggal(nik: String?, poin: Int): Agent? = repository.getListAgentTunggal(nik, poin)

    fun getListAgent(nik: String?, poin: Int): Agent? = repository.getListAgent(nik, poin)

    fun getListReseller(nik: String?, poin: Int): Agent? = repository.getListReseller(nik, poin)
}