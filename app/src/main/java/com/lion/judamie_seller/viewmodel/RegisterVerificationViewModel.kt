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

    // 인증 확인 버튼 활성화 여부
    private val _isVerificationConfirmButtonEnabled = MutableLiveData(false)
    val isVerificationConfirmButtonEnabled: LiveData<Boolean> = _isVerificationConfirmButtonEnabled

    init {
        // 인증번호 입력이 6자리가 될 때 활성화
        textFieldUserRegisterVerificationNumberEditTextText.observeForever { verificationNumber ->
            _isVerificationConfirmButtonEnabled.value = !verificationNumber.isNullOrBlank() && verificationNumber.length == 6
        }
    }

    // 인증 요청 버튼 클릭 이벤트
    fun buttonUserVerificationRequestOnClick() {
        val phoneNumber = textFieldUserRegisterPhoneNumberEditTextText.value

        // 휴대폰 번호 입력 검증
        if (phoneNumber.isNullOrBlank()) {
            phoneNumberError.value = "휴대폰 번호를 입력해주세요" // 에러 메시지 설정
        } else if (!phoneNumber.matches(Regex("^\\d{10,11}$"))) { // 숫자만 허용 (10~11자리)
            phoneNumberError.value = "올바른 휴대폰 번호를 입력해주세요" // 에러 메시지 설정
        } else {
            // 에러 메시지 제거
            phoneNumberError.value = null
            // 인증 요청 처리 로직
            registerVerificationFragment.proVerificationRequest()
        }
    }

    // 인증 확인 버튼 클릭 이벤트
    fun buttonUserVerificationConfirmOnClick() {
        val verificationNumber = textFieldUserRegisterVerificationNumberEditTextText.value

        if (verificationNumber.isNullOrBlank()) {
            verificationNumberError.value = "인증번호를 입력해주세요"
        } else if (!verificationNumber.matches(Regex("^\\d{6}$"))) {
            verificationNumberError.value = "올바른 인증번호를 입력해주세요"
        } else {
            verificationNumberError.value = null
            registerVerificationFragment.proVerificationConfirm()

            // 성공 시 가입 완료 버튼 활성화
            _isRegisterCompleteButtonEnabled.value = true
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