package com.lion.judamie_seller.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.fragment.RegisterFragment

class RegisterViewModel(val registerFragment: RegisterFragment) : ViewModel() {
    // toolbarRegister - title
    val toolbarUserLoginTitle = MutableLiveData<String>()
    // toolbarRegister - navigationIcon
    val toolbarUserLoginNavigationIcon = MutableLiveData<Int>()
    // textFieldUserRegisterIDEditTextText - EditText - text
    val textFieldUserRegisterIDEditTextText = MutableLiveData("")
    // textFieldUserRegisterPWEditTextText - EditText - text
    val textFieldUserRegisterPWEditTextText = MutableLiveData("")
    // textFieldUserRegisterPW2EditTextText - EditText - text
    val textFieldUserRegisterPW2EditTextText = MutableLiveData("")
    // textFieldUserRegisterStoreNameEditTextText - EditText - text
    val textFieldUserRegisterStoreNameEditTextText = MutableLiveData("")

    // 버튼 활성화 상태를 나타내는 LiveData
    private val _isRegisterCompleteButtonEnabled = MediatorLiveData<Boolean>()
    val isRegisterCompleteButtonEnabled: LiveData<Boolean> get() = _isRegisterCompleteButtonEnabled

    init {
        // 각 입력 필드의 변화를 감지하여 validateInputs 호출
        _isRegisterCompleteButtonEnabled.addSource(textFieldUserRegisterIDEditTextText) { validateInputs() }
        _isRegisterCompleteButtonEnabled.addSource(textFieldUserRegisterPWEditTextText) { validateInputs() }
        _isRegisterCompleteButtonEnabled.addSource(textFieldUserRegisterPW2EditTextText) { validateInputs() }
        _isRegisterCompleteButtonEnabled.addSource(textFieldUserRegisterStoreNameEditTextText) { validateInputs() }
    }

    private fun validateInputs() {
        _isRegisterCompleteButtonEnabled.value =
            !(textFieldUserRegisterIDEditTextText.value.isNullOrBlank() || textFieldUserRegisterPWEditTextText.value.isNullOrBlank() || textFieldUserRegisterPW2EditTextText.value.isNullOrBlank() || textFieldUserRegisterStoreNameEditTextText.value.isNullOrBlank())
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