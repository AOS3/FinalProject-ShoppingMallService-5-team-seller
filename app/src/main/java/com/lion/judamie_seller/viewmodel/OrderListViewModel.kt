package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.OrderListFragment

class OrderListViewModel(val orderListFragment: OrderListFragment) : ViewModel() {
    companion object{
        @JvmStatic
        @BindingAdapter("onNavigationClickOrderList")
        fun onNavigationClickOrderList(materialToolbar: MaterialToolbar, orderListFragment: OrderListFragment){
            materialToolbar.setNavigationOnClickListener {
                orderListFragment.movePrevFragment()
            }
        }
    }
}