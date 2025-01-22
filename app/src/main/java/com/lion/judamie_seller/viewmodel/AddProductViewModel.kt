package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.AddProductFragment

class AddProductViewModel(val addProductFragment: AddProductFragment) : ViewModel() {
    val textFieldProductNameEditTextText = MutableLiveData("")
    val productCategory = MutableLiveData<String>()
    val textFieldProductPriceEditTextText = MutableLiveData("")
    val textFieldProductDiscountRateEditTextText = MutableLiveData("")
    val textFieldProductStockEditTextText = MutableLiveData("")
    val textFieldProductDescriptionEditTextText = MutableLiveData("")

    companion object {
        @JvmStatic
        @BindingAdapter("onNavigationClickAddProduct")
        fun onNavigationClickAddProduct(
            materialToolbar: MaterialToolbar,
            addProductFragment: AddProductFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                addProductFragment.moveToProductManagementFragment()
            }
        }
    }
}