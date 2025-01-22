package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.ShowOneProductDetailFragment

class ShowOneProductDetailViewModel(val showOneProductDetailFragment: ShowOneProductDetailFragment) : ViewModel() {
    // textViewCustomer - text
    val textProductNameText = MutableLiveData("")
    // textViewOrderDate - text
    val textProductCategoryText = MutableLiveData("")
    // textViewProduct - text
    val textProductPriceText = MutableLiveData("")
    // textViewPrice - text
    val textViewProductDetailText = MutableLiveData("")

    companion object{
        @JvmStatic
        @BindingAdapter("onNavigationClickShowOneProductDetail")
        fun onNavigationClickShowOneProductDetail(materialToolbar: MaterialToolbar, showOneProductDetailFragment: ShowOneProductDetailFragment){
            materialToolbar.setNavigationOnClickListener {
                showOneProductDetailFragment.movePrevFragment()
            }
        }
    }
}