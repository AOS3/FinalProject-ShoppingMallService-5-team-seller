package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.RegisterVerificationFragment

class RegisterVerificationViewModel(val registerVerificationFragment: RegisterVerificationFragment) : ViewModel()  {
    //
    val textFieldUserRegisterPhoneNumberEditTextText = MutableLiveData("")
    //
    val textFieldUserRegisterVerificationNumberEditTextText = MutableLiveData("")
    // 휴대폰 번호 에러 메시지 관리
    val phoneNumberError = MutableLiveData<String?>()
    // 인증 번호 에러 메시지 관리
    val verificationNumberError = MutableLiveData<String?>()

    // 버튼 활성화 여부를 관리하는 LiveData
    private val _isRegisterCompleteButtonEnabled = MutableLiveData(false)
    val isRegisterCompleteButtonEnabled: LiveData<Boolean> = _isRegisterCompleteButtonEnabled

    // 인증 요청 버튼 클릭 이벤트
    fun buttonUserVerificationRequestOnClick() {
        val phoneNumber = textFieldUserRegisterPhoneNumberEditTextText.value

        // 휴대폰 번호 입력 검증
        if (phoneNumber.isNullOrBlank()) {
            phoneNumberError.value = "휴대폰 번호를 입력해주세요"
        } else {
            // 에러 메시지 제거
            phoneNumberError.value = null
            // 인증 요청 처리 로직
            registerVerificationFragment.proVerificationRequest()
        }
    }

    fun buttonUserVerificationConfirmOnClick(){
        val verificationNumber = textFieldUserRegisterVerificationNumberEditTextText.value

        // 휴대폰 번호 입력 검증
        if (verificationNumber.isNullOrBlank()) {
            verificationNumberError.value = "휴대폰 번호를 입력해주세요"
        } else {
            // 에러 메시지 제거
            verificationNumberError.value = null
            // 인증 요청 처리 로직
            registerVerificationFragment.proVerificationConfirm()
//            if (verificationNumber == "123456") {
            if( true) {
                _isRegisterCompleteButtonEnabled.value = true // 가입 완료 버튼 활성화
            } else {
                _isRegisterCompleteButtonEnabled.value = false // 실패 시 비활성화
            }
        }
    }

    fun buttonUserRegisterCompleteOnClick() {
        if (_isRegisterCompleteButtonEnabled.value == true) {
            registerVerificationFragment.proVerificationComplete()
        }
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