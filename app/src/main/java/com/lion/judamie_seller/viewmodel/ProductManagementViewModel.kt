package com.lion.judamie_seller.viewmodel

import android.view.View
import android.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.ProductManagementFragment

class ProductManagementViewModel(val productManagementFragment: ProductManagementFragment) : ViewModel() {
    // 검색 입력값 관리 (Two-way Binding 지원)
    val searchQuery = MutableLiveData<String>("") // MutableLiveData를 공개적으로 선언

    // 검색 필드 클리어 버튼 동작
    fun onClearSearchQuery() {
        searchQuery.value = "" // 검색 필드 초기화
    }

    // 검색 버튼 클릭 동작
    fun onSearchButtonClicked() {
        searchQuery.value?.let { query ->
            // 검색 요청 처리 로직
            performSearch(query)
        }
    }

    // 실제 검색 처리 로직 (예시)
    private fun performSearch(query: String) {
        println("검색 요청: $query")
    }

    companion object{
        // toolbarBoardRead - onNavigationClickBoardRead
        @JvmStatic
        @BindingAdapter("onNavigationClickProductManagement")
        fun onNavigationClickProductManagement(materialToolbar: MaterialToolbar, productManagementFragment: ProductManagementFragment){
            materialToolbar.setNavigationOnClickListener {
                productManagementFragment.movePrevFragment()
            }
        }
    }
}