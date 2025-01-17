package com.lion.judamie_seller.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lion.judamie_seller.fragment.SalesListFragment

class RowSalesListViewModel (val salesListFragment: SalesListFragment) : ViewModel(){
    // textViewSalesList - text
    val textViewSalesList = MutableLiveData("")
}