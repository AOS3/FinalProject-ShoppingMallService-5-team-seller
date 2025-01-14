package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.RegisterFragment

class RegisterViewModel(val registerFragment: RegisterFragment) : ViewModel() {
    // toolbarUserJoinStep1 - title
    val toolbarUserLoginTitle = MutableLiveData<String>()
    // toolbarUserJoinStep1 - navigationIcon
    val toolbarUserLoginNavigationIcon = MutableLiveData<Int>()
    // textFieldUserRegisterIDEditTextText - EditText - text
    val textFieldUserRegisterIDEditTextText = MutableLiveData("")
    // textFieldUserRegisterPWEditTextText - EditText - text
    val textFieldUserRegisterPWEditTextText = MutableLiveData("")
    // textFieldUserRegisterPW2EditTextText - EditText - text
    val textFieldUserRegisterPW2EditTextText = MutableLiveData("")
    // textFieldUserRegisterStoreNameEditTextText - EditText - text
    val textFieldUserRegisterStoreNameEditTextText = MutableLiveData("")

    private val _isRegisterCompleteButtonEnabled = MutableLiveData(false)
    val isRegisterCompleteButtonEnabled: LiveData<Boolean> = _isRegisterCompleteButtonEnabled

    fun buttonActive() {
        // ID, PW 입력 검증
        val IDNum = textFieldUserRegisterIDEditTextText.value
        val PWNum1 =textFieldUserRegisterPWEditTextText.value
        val PWNum2 =textFieldUserRegisterPW2EditTextText.value

        // 휴대폰 번호 입력 검증
        if (IDNum.isNullOrBlank() || PWNum1.isNullOrBlank() || PWNum2.isNullOrBlank()) {
            _isRegisterCompleteButtonEnabled.value = false
        } else {
            _isRegisterCompleteButtonEnabled.value = true
        }
    }
    fun buttonUserVerificationOnClick(){
        registerFragment.moveToUserVerification()
    }

    companion object{
        @JvmStatic
        @BindingAdapter("onNavigationClickUserRegister")
        fun onNavigationClickUserRegister(materialToolbar: MaterialToolbar, registerFragment: RegisterFragment){
            materialToolbar.setNavigationOnClickListener {
                registerFragment.movePrevFragment()
            }
        }
    }
}