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

    // buttonUserLoginJoin - onClick
    fun buttonUserRegisterOnClick(){
        loginFragment.moveToUserRegister()
    }

    // buttonUserLoginSubmit - onClick
    fun buttonUserLoginSubmitOnClick(){
        loginFragment.proLogin()
    }
}