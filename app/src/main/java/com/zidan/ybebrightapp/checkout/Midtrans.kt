package com.zidan.ybebrightapp.checkout

import android.content.Context
import com.zidan.ybebrightapp.BuildConfig
import com.zidan.ybebrightapp.model.Order
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

class Midtrans(private val ctx: Context) {

    companion object {
        fun getInstance(ctx: Context, callback: TransactionFinishedCallback): Midtrans {
            SdkUIFlowBuilder.init()
                .setClientKey(BuildConfig.CLIENT_KEY) // client_key is mandatory
                .setContext(ctx) // context is mandatory
                .setTransactionFinishedCallback(callback) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(BuildConfig.BASE_URL) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .buildSDK()
            return Midtrans(ctx)
        }
    }

    fun createTransaction(id: String, amount: Int, data: Order, address: String, city: String, courier: String) {

        val transaction = TransactionRequest(id, amount.toDouble())

        val customerDetails = CustomerDetails()
        customerDetails.firstName = data.name
        customerDetails.phone = data.phone

        val shippingAddress = ShippingAddress()
        shippingAddress.address = address
        shippingAddress.city = city
        customerDetails.shippingAddress = shippingAddress

        val billingAddress = BillingAddress()
        billingAddress.address = address
        billingAddress.city = city
        customerDetails.billingAddress = billingAddress

        transaction.customerDetails = customerDetails

        val listItem = arrayListOf<ItemDetails>()
        data.buy?.forEach {
            val itemDetails = ItemDetails(it.name, it.price.toDouble(), it.count, it.name)
            listItem.add(itemDetails)
        }

        listItem.add(
            ItemDetails(
                "Ongkir",
                data.cost!!.toDouble(),
                1,
                courier + " " + data.courier
            )
        )

        transaction.itemDetails = listItem

        MidtransSDK.getInstance().transactionRequest = transaction
        MidtransSDK.getInstance().startPaymentUiFlow(ctx)
    }
}