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

    fun buttonUserVerificationOnClick(){
        registerFragment.apply {
            // 입력요소 검사
            if(textFieldUserRegisterIDEditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterID.editText!!)
                }
                return
            }
            if(textFieldUserRegisterPWEditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterPW.editText!!)
                }
                return
            }
            if(textFieldUserRegisterPW2EditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterPW2.editText!!)
                }
                return
            }
            if(textFieldUserRegisterPWEditTextText.value != textFieldUserRegisterPW2EditTextText.value){
                userActivity.showMessageDialog("비밀번호 입력", "비밀번호가 다릅니다", "확인"){
                    textFieldUserRegisterPWEditTextText.value = ""
                    textFieldUserRegisterPW2EditTextText.value = ""
                    userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterPW.editText!!)
                }
                return
            }
            if(textFieldUserRegisterStoreNameEditTextText.value?.isEmpty()!!){
                userActivity.showMessageDialog("스토어 이름 입력", "스토어 이름을 입력해주세요", "확인"){
                    userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterStoreName.editText!!)
                }
                return
            }

            // 중복확인을 안했다면..
            if(!registerFragment.isCheckRegisterUserIdExist){
                userActivity.showMessageDialog("아이디 중복 확인", "아이디 중복 확인을 해주세요", "확인"){

                }
                return
            }

            // 다음 화면으로 이동한다.
            moveToUserVerification()
        }
    }

    // buttonUserJoinStep1CheckId - onClick
    fun buttonUserRegisterCheckIdOnClick(){
        registerFragment.checkRegisterUserId()
    }

    // textFieldUserJoinStep1Id - onTextChanged
    fun textFieldUserRegisterIdOnTextChanged(){
        registerFragment.isCheckRegisterUserIdExist = false
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