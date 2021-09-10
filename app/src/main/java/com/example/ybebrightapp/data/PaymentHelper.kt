package com.example.ybebrightapp.data
//
//import android.content.Context
//import com.example.ybebrightapp.model.Agent
//import com.example.ybebrightapp.model.Buy
//import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
//import com.midtrans.sdk.uikit.SdkUIFlowBuilder
//import com.midtrans.sdk.corekit.core.TransactionRequest
//import com.midtrans.sdk.corekit.models.*
//
//class PaymentHelper(private val ctx: Context?) {
//
//    companion object {
//        private const val BASE_URL = "\"https://merchant-url-sandbox.com/\""
//        private const val CLIENT_KEY = "SB-Mid-client-x6A1rUcEyIJOBtMU"
//    }
//
//    fun init() {
//        SdkUIFlowBuilder.init()
//            .setClientKey(CLIENT_KEY) // client_key is mandatory
//            .setContext(ctx) // context is mandatory
//            .setTransactionFinishedCallback {
//                // Handle finished transaction here.
//            }
//            .setMerchantBaseUrl(BASE_URL) //set merchant url (required)
//            .enableLog(true) // enable sdk log (optional)
//            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // set theme. it will replace theme on snap theme on MAP ( optional)
//            .setLanguage("id") //`en` for English and `id` for Bahasa
//            .buildSDK()
//    }
//
//    fun pay(id: String, total: Double, data: Agent, buy: List<Buy>) {
//        val transactionRequest = TransactionRequest(id, total)
//
//        val customerDetail = CustomerDetails()
//        customerDetail.customerIdentifier = "budi-6789"
//        customerDetail.phone = data.phone
//        customerDetail.firstName = data.name
//        customerDetail.email = data.email
//
//        val shippingAddress = ShippingAddress()
//        shippingAddress.address = data.address
//        shippingAddress.city = "Jakarta"
//        shippingAddress.postalCode = "10220"
//        customerDetail.shippingAddress = shippingAddress
//
//        val itemDetailsList = arrayListOf<ItemDetails>()
//
//        for (item in buy) {
//            itemDetailsList.add(ItemDetails("1", item.price.toDouble(), item.count, item.name))
//        }
//
//        transactionRequest.apply {
//            customerDetails = customerDetail
//            itemDetails = itemDetailsList
//        }
//
//    }
//}