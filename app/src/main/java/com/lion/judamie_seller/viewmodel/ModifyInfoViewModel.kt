package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.ModifyInfoFragment

class ModifyInfoViewModel(val modifyInfoFragment: ModifyInfoFragment) : ViewModel() {

    companion object{
        @JvmStatic
        @BindingAdapter("onNavigationClickModifyInfo")
        fun onNavigationClickModifyInfo(materialToolbar: MaterialToolbar, modifyInfoFragment: ModifyInfoFragment){
            materialToolbar.setNavigationOnClickListener {
                modifyInfoFragment.movePrevFragment()
            }
        }
    }
}