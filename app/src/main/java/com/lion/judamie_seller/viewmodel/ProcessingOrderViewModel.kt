package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.ProcessingOrderFragment

class ProcessingOrderViewModel(val processingOrderFragment: ProcessingOrderFragment) : ViewModel() {

    // textViewCustomer - text
    val textViewCustomerText = MutableLiveData("")
    // textViewOrderDate - text
    val textViewOrderDateText = MutableLiveData("")
    // textViewProduct - text
    val textViewProductText = MutableLiveData("")
    // textViewPrice - text
    val textViewPriceText = MutableLiveData("")
    // textViewQuantity - text
    val textViewQuantityText = MutableLiveData("")
    // textViewQuantity - text
    val textViewPickupLocation = MutableLiveData("")
    // textViewDelivery - text
    val textViewDeliveryText = MutableLiveData("")
    // textViewPickup - text
    val textViewPickupText = MutableLiveData("")
    // textViewDepositText - text
    val textViewDepositText = MutableLiveData("")

    // buttonDeposit - onClick
    fun buttonDepositOnClick(){
        processingOrderFragment.buttonDeposit()
    }

    companion object{
        @JvmStatic
        @BindingAdapter("onNavigationClickProcessingOrder")
        fun onNavigationClickProcessingOrder(materialToolbar: MaterialToolbar, processingOrderFragment: ProcessingOrderFragment){
            materialToolbar.setNavigationOnClickListener {
                processingOrderFragment.movePrevFragment()
            }
        }
    }
}