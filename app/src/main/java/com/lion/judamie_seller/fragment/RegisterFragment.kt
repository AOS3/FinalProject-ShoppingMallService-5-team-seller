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
import com.lion.judamie_seller.databinding.FragmentRegisterBinding
import com.lion.judamie_seller.viewmodel.RegisterViewModel


class RegisterFragment : Fragment() {

    lateinit var userActivity: UserActivity
    lateinit var fragmentRegisterViewBinding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRegisterViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        fragmentRegisterViewBinding.registerViewModel = RegisterViewModel(this@RegisterFragment)
        fragmentRegisterViewBinding.lifecycleOwner = this@RegisterFragment

        userActivity = activity as UserActivity

        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbar()

        return fragmentRegisterViewBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentRegisterViewBinding.registerViewModel?.apply {
            // 타이틀
            toolbarUserLoginTitle.value = "회원 가입"
            // 네비게이션 아이콘
            toolbarUserLoginNavigationIcon.value = R.drawable.arrow_back_24px

        }
    }

    fun moveToUserVerification(){
        userActivity.replaceFragment(UserFragmentName.USER_REGISTER_VERIFICATION_FRAGMENT, true, true, null)
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        userActivity.removeFragment(UserFragmentName.USER_REGISTER_FRAGMENT)
    }


}