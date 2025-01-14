package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lion.judamie_seller.UserActivity
import com.lion.judamie_seller.UserFragmentName
import com.lion.judamie_seller.databinding.FragmentRegisterVerificationBinding
import com.lion.judamie_seller.viewmodel.RegisterVerificationViewModel


class RegisterVerificationFragment : Fragment() {
    lateinit var userActivity: UserActivity
    private lateinit var binding: FragmentRegisterVerificationBinding
    private val viewModel: RegisterVerificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 데이터 바인딩 객체 생성
        binding = FragmentRegisterVerificationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.registerVerificationViewModel = viewModel

        userActivity = activity as UserActivity

        // 에러 메시지 표시
        viewModel.phoneNumberError.observe(viewLifecycleOwner, Observer { errorMessage ->
            binding.textFieldUserPhoneNumber.error = errorMessage
        })

        viewModel.verificationNumberError.observe(viewLifecycleOwner, Observer { errorMessage ->
            binding.textFieldUserVerificationNumber.error = errorMessage
        })

        return binding.root
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