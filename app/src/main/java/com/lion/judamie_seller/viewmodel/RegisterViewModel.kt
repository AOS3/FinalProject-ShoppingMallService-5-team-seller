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

    // buttonLoginNextStep - onClick
    fun buttonVerificationOnClick() {
        registerFragment.apply {

            // 입력 요소 검사
            if (textFieldUserRegisterIDEditTextText.value?.isEmpty()!!) {
                // 아이디 입력란에 에러 메시지 표시
                fragmentRegisterViewBinding.textFieldUserRegisterID.error = "아이디를 입력해주세요"
                userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterID.editText!!)
                return
            }

            if (textFieldUserRegisterPWEditTextText.value?.isEmpty()!!) {
                // 비밀번호 입력란에 에러 메시지 표시
                fragmentRegisterViewBinding.textFieldUserRegisterPW.error = "비밀번호를 입력해주세요"
                userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterPW.editText!!)
                return
            }

            if (textFieldUserRegisterPW2EditTextText.value?.isEmpty()!!) {
                // 비밀번호 확인 입력란에 에러 메시지 표시
                fragmentRegisterViewBinding.textFieldUserRegisterPW2.error = "비밀번호를 입력해주세요"
                userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterPW2.editText!!)
                return
            }

            if (textFieldUserRegisterPWEditTextText.value != textFieldUserRegisterPW2EditTextText.value) {
                // 비밀번호 불일치 에러 메시지 표시
                fragmentRegisterViewBinding.textFieldUserRegisterPW.error = "비밀번호가 다릅니다"
                fragmentRegisterViewBinding.textFieldUserRegisterPW2.error = "비밀번호가 다릅니다"
                fragmentRegisterViewBinding.registerViewModel?.textFieldUserRegisterPWEditTextText?.value = ""
                fragmentRegisterViewBinding.registerViewModel?.textFieldUserRegisterPW2EditTextText?.value = ""
                userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterPW.editText!!)
                return
            }

            // 입력 요소 검사
            if (textFieldUserRegisterStoreNameEditTextText.value?.isEmpty()!!) {
                // 아이디 입력란에 에러 메시지 표시
                fragmentRegisterViewBinding.textFieldUserRegisterStoreName.error = "이름을 입력해주세요"
                userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterStoreName.editText!!)
                return
            }

            // 중복 확인
            checkRegisterIdName()
            // 다음 화면으로 이동
            moveToUserVerification()
        }
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