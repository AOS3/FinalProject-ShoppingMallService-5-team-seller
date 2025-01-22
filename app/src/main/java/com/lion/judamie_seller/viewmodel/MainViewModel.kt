package com.lion.judamie_seller.viewmodel

import androidx.lifecycle.ViewModel
import com.lion.judamie_seller.fragment.MainFragment

class MainViewModel(val mainFragment: MainFragment) : ViewModel() {

    fun buttonProductManagementOnClick(){
        mainFragment.moveToProductManagement()
    }

    fun buttonOrderListOnClick(){
        mainFragment.moveToOrderList()
    }

    fun buttonSalesListOnClick(){
        mainFragment.moveToSalesList()
    }

    fun buttonModifyInfoOnClick(){
        mainFragment.moveToModifyInfo()
    }
}