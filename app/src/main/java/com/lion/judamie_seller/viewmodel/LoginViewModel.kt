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
    fun buttonUserLoginSubmitOnClick() {
        val IDNum = textFieldUserLoginIdEditTextText.value
        val PWNum = textFieldUserLoginPwEditTextText.value

        // 아이디와 비밀번호 입력 검증
        IDError.value = if (IDNum.isNullOrBlank()) "아이디를 입력해주세요" else null
        PWError.value = if (PWNum.isNullOrBlank()) "비밀번호를 입력해주세요" else null

        // 모든 필드가 올바르게 입력되었을 경우
        if (IDError.value == null && PWError.value == null) {
            loginFragment.proLogin()
        }
    }
}