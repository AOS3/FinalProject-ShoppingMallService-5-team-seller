package com.lion.judamie_seller.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lion.judamie_seller.fragment.OrderCategoryFragment
import com.lion.judamie_seller.fragment.OrderListFragment

class RowOrderCategoryListViewModel(val orderCategoryFragment: OrderCategoryFragment) : ViewModel() {
    // textViewSalesList - text
    val textViewOrderListCategoryList = MutableLiveData("")
}