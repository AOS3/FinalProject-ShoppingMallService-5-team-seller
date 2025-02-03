package com.lion.judamie_seller.viewmodel

import android.os.Build.VERSION_CODES.M
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.ModifyInfoFragment

class ModifyInfoViewModel(val ModifyInfoFragment: ModifyInfoFragment) : ViewModel() {
    //
    val textFieldPhoneNumber = MutableLiveData("")
    //
    val textFieldVerificationText = MutableLiveData("")
    //
    val textNewPasswordText = MutableLiveData("")

    val textNewPassword2Text = MutableLiveData("")

    // 인증 요청 버튼
    fun buttonVerificationRequestVerificationOnClick(view: View){
        val phoneNumber1 = textFieldPhoneNumber.value
        if (phoneNumber1.isNullOrEmpty()) {
            ModifyInfoFragment.fragmentModifyInfoBinding.textFieldPhoneNumber.error = "전화번호를 입력해주세요"
        } else {
            val phoneNumber = "+82" + phoneNumber1
            ModifyInfoFragment.sendVerificationCode(phoneNumber)
        }
    }

    // 인증 확인 버튼
    fun buttonVerificationCheckInfoOnClick(view: View) {
        ModifyInfoFragment.verificationCheck()
    }

    fun buttonChangeCompleteOnClick(view: View) {
        // 가입 완료 처리 메서드 호출
        ModifyInfoFragment.proChange()
    }

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