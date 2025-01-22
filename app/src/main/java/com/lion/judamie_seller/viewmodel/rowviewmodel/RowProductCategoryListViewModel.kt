package com.lion.judamie_seller.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lion.judamie_seller.fragment.ProductCategoryFragment

class RowProductCategoryListViewModel(val productCategoryFragment: ProductCategoryFragment) : ViewModel() {
    // textViewSalesList - text
    val textViewProductCategoryList = MutableLiveData("")
}