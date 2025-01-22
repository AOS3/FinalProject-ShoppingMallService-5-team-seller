package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.ProcessingOrderFragment
import com.lion.judamie_seller.fragment.ShowOneSalesDetailFragment

class ShowOneSalesDetailViewModel(val showOneSalesDetailFragment: ShowOneSalesDetailFragment) : ViewModel() {
    // textViewCustomer - text
    val textViewCustomerText = MutableLiveData("")
    // textViewOrderDate - text
    val textViewOrderDateText = MutableLiveData("")
    // textViewProduct - text
    val textViewTransactionDateText = MutableLiveData("")
    // textViewPrice - text
    val textViewProductText = MutableLiveData("")
    // textViewQuantity - text
    val textViewQuantityText = MutableLiveData("")
    // textViewQuantity - text
    val textViewPriceText = MutableLiveData("")
    // textViewDelivery - text
    val textViewTotalPriceText = MutableLiveData("")

    companion object{
        @JvmStatic
        @BindingAdapter("onNavigationClickShowOneSalesDetail")
        fun onNavigationClickShowOneSalesDetail(materialToolbar: MaterialToolbar, showOneSalesDetailFragment: ShowOneSalesDetailFragment){
            materialToolbar.setNavigationOnClickListener {
                showOneSalesDetailFragment.movePrevFragment()
            }
        }
    }
}