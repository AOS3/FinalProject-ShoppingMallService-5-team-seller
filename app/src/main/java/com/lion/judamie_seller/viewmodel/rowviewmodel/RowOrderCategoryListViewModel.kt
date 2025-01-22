package com.lion.judamie_seller.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lion.judamie_seller.fragment.OrderListFragment

class RowOrderCategoryListViewModel(val orderListFragment: OrderListFragment) : ViewModel() {
    // textViewSalesList - text
    val textViewOrderListCategoryList = MutableLiveData("")
}