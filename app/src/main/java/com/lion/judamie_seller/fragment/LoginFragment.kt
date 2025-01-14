package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.judamie_seller.R
import com.lion.judamie_seller.UserActivity
import com.lion.judamie_seller.UserFragmentName
import com.lion.judamie_seller.databinding.FragmentLoginBinding
import com.lion.judamie_seller.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var userActivity: UserActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        fragmentLoginBinding.loginViewModel = LoginViewModel(this@LoginFragment)
        fragmentLoginBinding.lifecycleOwner = this@LoginFragment

        userActivity = activity as UserActivity
        // 툴바 설정
        settingToolbar()

        // 리스너 설정
        fragmentLoginBinding.buttonUserLoginSubmit.setOnClickListener {
            // 로그인 처리 로직
        }

        return fragmentLoginBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentLoginBinding.loginViewModel?.apply {
            toolbarUserLoginTitle.value = "로그인"
        }
    }

    // 회원 가입 화면으로 이동시키는 메서드
    fun moveToUserRegister(){
        userActivity.replaceFragment(UserFragmentName.USER_REGISTER_FRAGMENT, true, true, null)
    }

    fun proLogin() {

    }
}