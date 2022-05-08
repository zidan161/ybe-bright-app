package com.zidan.ybebrightapp.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zidan.ybebrightapp.data.JsonHelper
import com.zidan.ybebrightapp.data.MainRepository
import com.zidan.ybebrightapp.model.*

class ProductViewModel(private val repository: MainRepository, private val helper: JsonHelper): ViewModel() {

    var listItem = MutableLiveData<MutableList<Buy>>()
    val list = mutableListOf<Buy>()
    var address = MutableLiveData<String>()
    var courier = "JNE"
    var sellerId = ""

    fun addItem(item: Buy, status: String) {
        if (list.isNotEmpty()) {
            val countAll = reCount(item, status, true)
            list.find { it.name == item.name }.let {
                if (it != null) {
                    it.count += item.count
                    val price = getPrice(it.isPaket, it.name, countAll, status)
                    it.price = price
                    it.total = it.count * it.price
                    listItem.value = list
                } else {
                    item.price = getPrice(item.isPaket, item.name, countAll, status)
                    list.add(item)
                    listItem.value = list
                }
            }
        } else {
            list.add(item)
            listItem.value = list
        }
    }

    private fun getPrice(isPaket: Boolean, name: String, qty: Int, status: String): Int {

        val data = if (isPaket) {
            helper.loadPrice("harga_paket.json", status)
        } else {
            helper.loadPrice("harga_satuan.json", name)
        }

        val prices = if (isPaket) { data }
        else {
            when (status) {
                "Agen Tunggal" -> arrayListOf(data[3])
                "Agen" -> arrayListOf(data[2])
                "Reseller" -> arrayListOf(data[1])
                else -> arrayListOf(data[0])
            }
        }

        if (isPaket) {
            if (status == "Agen Tunggal") {
                return prices[prices.lastIndex].harga
            } else {
                for (i in prices) {
                    if (i.qty.contains("-")) {
                        val range = toRange(i.qty)
                        if (qty in range) {
                            return i.harga
                        }
                    } else {
                        return i.harga
                    }
                }
            }
        }
        return prices[0].harga
    }

    fun reCount(item: Buy, status: String, isAdd: Boolean): Int {
        var countAll = 0
        if (isAdd) countAll += item.count
        if (item.isPaket) {
            val allPaket = list.filter { data -> data.isPaket }
            allPaket.forEach {
                countAll += it.count
            }
            for (i in allPaket) {
                i.price = getPrice(i.isPaket, i.name, countAll, status)
            }
            allPaket.size + item.count
        } else {
            val allSatuan = list.filter { data -> !data.isPaket }
            allSatuan.forEach {
                countAll += it.count
            }
            for (i in allSatuan) {
                i.price = getPrice(i.isPaket, i.name, countAll, status)
            }
        }
        return countAll
    }

    private fun toRange(string: String): IntRange {
        return string.split("-").let { (a, b) -> a.toInt()..b.toInt() }
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

    fun getProvinces(): Pair<LiveData<List<Province>>, String?> = repository.getProvince()

    fun getCosts(origin: String, cityId: String, weight: Int, courier: String): Pair<LiveData<List<Costs>>, LiveData<String?>> =
        repository.getCost(origin, cityId, weight, courier)

    fun setOngkir(ongkir: Costs) {
        this.ongkir.value = ongkir
    }

    fun getAllCity(): Pair<LiveData<List<City>>, String?> = repository.getAllCity()

    fun getMemberByValue(id: String?): MutableLiveData<MutableList<Cabang>?> = repository.getMemberByValue(id)
}