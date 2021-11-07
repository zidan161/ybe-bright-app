package com.example.ybebrightapp.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.data.MainRepository
import com.example.ybebrightapp.model.Buy
import com.example.ybebrightapp.model.City
import com.example.ybebrightapp.model.Costs
import com.example.ybebrightapp.model.Province
import okhttp3.RequestBody

class ProductViewModel(private val repository: MainRepository): ViewModel() {

    var listItem = MutableLiveData<MutableList<Buy>>()
    val list = mutableListOf<Buy>()
    var address = MutableLiveData<String>()
    var courier = "JNE"

    fun addItem(item: Buy) {
        if (list.isNotEmpty()) {
            for (it in list) {
                if (it.name == item.name) {
                    it.count += item.count
                    it.total = it.count * it.price
                    listItem.value = list
                    break
                } else {
                    list.add(item)
                    listItem.value = list
                    break
                }
            }
        } else {
            list.add(item)
            listItem.value = list
        }
    }

    fun removeItem(position: Int){
    }

    //Checkout
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