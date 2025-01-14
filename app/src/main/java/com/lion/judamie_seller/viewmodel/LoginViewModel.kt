package com.lion.judamie_seller.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lion.judamie_seller.fragment.LoginFragment

data class LoginViewModel(val loginFragment: LoginFragment) : ViewModel() {
    // toolbarUserLogin - title
    val toolbarUserLoginTitle = MutableLiveData("")
    // textFieldUserLoginId - EditText - text
    val textFieldUserLoginIdEditTextText = MutableLiveData("")
    // textFieldUserLoginPw - EditText - text
    val textFieldUserLoginPwEditTextText = MutableLiveData("")
    // checkBoxUserLoginAuto - checked
    val checkBoxUserLoginAutoChecked = MutableLiveData(false)
    // ID 에러 메시지 관리
    val IDError = MutableLiveData<String?>()
    // PW 에러 메시지 관리
    val PWError = MutableLiveData<String?>()

    // buttonUserLoginJoin - onClick
    fun buttonUserRegisterOnClick(){
        loginFragment.moveToUserRegister()
    }

    // buttonUserLoginSubmit - onClick
    fun buttonUserLoginSubmitOnClick(){
        val IDNum = textFieldUserLoginIdEditTextText.value
        val PWNum =textFieldUserLoginPwEditTextText.value

        // 휴대폰 번호 입력 검증
        if (IDNum.isNullOrBlank() || PWNum.isNullOrBlank()) {
            IDError.value = "아이디를 입력해주세요"
            PWError.value = "비밀번호를 입력해주세요"
        } else {
            // 에러 메시지 제거
            IDError.value = null
            PWError.value = null
            // 인증 요청 처리 로직
            loginFragment.proLogin()
        }
    }
}