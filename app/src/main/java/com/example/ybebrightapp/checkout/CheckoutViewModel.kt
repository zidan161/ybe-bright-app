package com.example.ybebrightapp.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.data.MainRepository
import com.example.ybebrightapp.model.City
import com.example.ybebrightapp.model.Cost
import com.example.ybebrightapp.model.Costs
import com.example.ybebrightapp.model.Province
import okhttp3.RequestBody

class CheckoutViewModel(private val repository: MainRepository): ViewModel() {

    val id = MutableLiveData<String>()
    val ongkir = MutableLiveData<Costs>()

    fun setId(id: String) {
        this.id.value = id
    }

    val cities: LiveData<List<City>> = Transformations.switchMap(id) { id ->
        repository.getCity(id)
    }

    fun getProvinces(): LiveData<List<Province>> = repository.getProvince()

    fun getCosts(request: RequestBody): LiveData<List<Costs>> =
        repository.getCost(request)

    fun setOngkir(ongkir: Costs) {
        this.ongkir.value = ongkir
    }
}