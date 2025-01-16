package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.SalesListFragment

class SalesListViewModel(val salesListFragment: SalesListFragment) : ViewModel() {

    val categories = listOf("소주", "맥주", "위스키")
    private val _salesData = MutableLiveData<List<String>>()
    val salesData: LiveData<List<String>> = _salesData

    fun loadCategoryData(categoryIndex: Int) {
        _salesData.value = when (categoryIndex) {
            0 -> listOf("소주1", "소주2", "소주3")
            1 -> listOf("맥주1", "맥주2", "맥주3")
            2 -> listOf("위스키1", "위스키2", "위스키3")
            else -> emptyList()
        }
    }

    companion object{
        // toolbarBoardRead - onNavigationClickBoardRead
        @JvmStatic
        @BindingAdapter("onNavigationClickSalesList")
        fun onNavigationClickSalesList(materialToolbar: MaterialToolbar, salesListFragment: SalesListFragment){
            materialToolbar.setNavigationOnClickListener {
                salesListFragment.movePrevFragment()
            }
        }
    }
}