package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.SalesListFragment

class SalesListViewModel(val salesListFragment: SalesListFragment) : ViewModel() {

    companion object{
        // toolbarProductRead - onNavigationClickProductRead
        @JvmStatic
        @BindingAdapter("onNavigationClickSalesList")
        fun onNavigationClickSalesList(materialToolbar: MaterialToolbar, salesListFragment: SalesListFragment){
            materialToolbar.setNavigationOnClickListener {
                salesListFragment.movePrevFragment()
            }
        }
    }
}