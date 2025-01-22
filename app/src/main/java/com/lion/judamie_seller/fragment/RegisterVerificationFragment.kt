package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lion.judamie_seller.R
import com.lion.judamie_seller.UserActivity
import com.lion.judamie_seller.UserFragmentName
import com.lion.judamie_seller.databinding.FragmentRegisterVerificationBinding
import com.lion.judamie_seller.viewmodel.RegisterVerificationViewModel


class RegisterVerificationFragment : Fragment() {
    lateinit var fragmentRegisterVerificationBinding: FragmentRegisterVerificationBinding
    lateinit var userActivity: UserActivity


    // 번들로 전달된 데이터를 담을 변수
    lateinit var userId:String
    lateinit var userPw:String

    // 닉네임 중복 확인을 했는지 확인하기 위한 변수
    var isCheckUserNickExist = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRegisterVerificationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_verification, container, false)
        fragmentRegisterVerificationBinding.registerVerificationViewModel = RegisterVerificationViewModel(this@RegisterVerificationFragment)
        fragmentRegisterVerificationBinding.lifecycleOwner = this@RegisterVerificationFragment

        userActivity = activity as UserActivity

        return fragmentRegisterVerificationBinding.root
    }
    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        userActivity.removeFragment(UserFragmentName.USER_REGISTER_VERIFICATION_FRAGMENT)
    }

    fun proVerificationRequest(){

    }

    fun proVerificationConfirm(){

    }

    fun proVerificationComplete() {
    }
}