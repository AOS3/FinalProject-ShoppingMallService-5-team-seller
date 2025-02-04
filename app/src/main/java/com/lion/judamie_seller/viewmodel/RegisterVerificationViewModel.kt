package com.lion.judamie_seller.viewmodel

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.RegisterVerificationFragment

class RegisterVerificationViewModel(val registerVerificationFragment: RegisterVerificationFragment) : ViewModel()  {
    val toolbarRegisterVerificationTitle = MutableLiveData<String>()
    // toolbarRegisterVerification - navigationIcon
    val toolbarRegisterVerificationNavigationIcon = MutableLiveData<Int>()
    //
    val textFieldUserRegisterPhoneNumberEditTextText = MutableLiveData("")
    //
    val textFieldUserRegisterVerificationNumberEditTextText = MutableLiveData("")

    // 인증 요청 버튼
    // buttonRegisterVerificationRequestVerification - onClick
    fun buttonRegisterVerificationRequestVerificationOnClick(view: View){
        val phoneNumber1 = textFieldUserRegisterPhoneNumberEditTextText.value
        if (phoneNumber1.isNullOrEmpty()) {
            registerVerificationFragment.fragmentRegisterVerificationBinding.textFieldUserPhoneNumber.error = "전화번호를 입력해주세요"
        } else {
            val phoneNumber = "+82" + phoneNumber1
            registerVerificationFragment.sendVerificationCode(phoneNumber)
        }
    }

    // 인증 확인 버튼
    // buttonRegisterVerificationCheckInfo - onClick
    fun buttonRegisterVerificationCheckInfoOnClick(view: View) {
        registerVerificationFragment.verificationCheck()
    }

    fun buttonUserRegisterCompleteOnClick(view: View) {
        // 가입 완료 처리 메서드 호출
        registerVerificationFragment.proUserJoin()
    }

    companion object{
        @JvmStatic
        @BindingAdapter("onNavigationClickUserRegisterVerification")
        fun onNavigationClickUserRegisterVerification(materialToolbar: MaterialToolbar, registerVerificationFragment: RegisterVerificationFragment){
            materialToolbar.setNavigationOnClickListener {
                registerVerificationFragment.movePrevFragment()
            }
        }
    }
}