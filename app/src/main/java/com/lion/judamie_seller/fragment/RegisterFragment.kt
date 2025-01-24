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
import com.lion.judamie_seller.service.UserService
import com.lion.judamie_seller.viewmodel.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


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
        fragmentRegisterViewBinding.apply {
            // 사용자가 입력한 데이터를 가져온다.
            val sellerId = registerViewModel?.textFieldUserRegisterIDEditTextText?.value!!
            val sellerPw = registerViewModel?.textFieldUserRegisterPWEditTextText?.value!!
            val storeName =
                registerViewModel?.textFieldUserRegisterStoreNameEditTextText?.value!!
            // 데이터를 담는다.
            val dataBundle = Bundle()
            dataBundle.putString("sellerId", sellerId)
            dataBundle.putString("sellerPw", sellerPw)
            dataBundle.putString("storeName", storeName)
            userActivity.replaceFragment(
                UserFragmentName.USER_REGISTER_VERIFICATION_FRAGMENT,
                true,
                true,
                dataBundle
            )
        }
    }

    // 이름, 아이디 중복 처리 메서드
    fun checkRegisterIdName() {
        // 사용자가 입력한 아이디
        val sellerId = fragmentRegisterViewBinding.registerViewModel?.textFieldUserRegisterIDEditTextText?.value!!
        // 사용자가 입력한 이름
        val storeName = fragmentRegisterViewBinding.registerViewModel?.textFieldUserRegisterStoreNameEditTextText?.value!!

        // 사용할 수 있는 아이디인지 검사
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 아이디 중복 체크
                val isIdAvailable = UserService.checkJoinUserId(sellerId)
                // 이름 중복 체크
                val isNameAvailable = UserService.checkJoinUserName(storeName)

                Pair(isIdAvailable, isNameAvailable)
            }

            val (isIdAvailable, isNameAvailable) = work1.await()

            // 아이디 중복 여부 처리
            if (isIdAvailable) {
                // 사용할 수 있는 아이디일 때는 에러 메시지 제거
                fragmentRegisterViewBinding.textFieldUserRegisterID.error = null
            } else {
                // 이미 존재하는 아이디일 때 에러 메시지 표시
                fragmentRegisterViewBinding.textFieldUserRegisterID.error = "이미 존재하는 아이디입니다"
                // 소프트 키보드 표시
                userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterID)
            }

            // 이름 중복 여부 처리
            if (isNameAvailable) {
                // 사용할 수 있는 이름일 때는 에러 메시지 제거
                fragmentRegisterViewBinding.textFieldUserRegisterStoreName.error = null
            } else {
                // 이미 존재하는 이름일 때 에러 메시지 표시
                fragmentRegisterViewBinding.textFieldUserRegisterStoreName.error = "이미 존재하는 이름입니다"
                // 소프트 키보드 표시
                userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterStoreName)
            }
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        userActivity.removeFragment(UserFragmentName.USER_REGISTER_FRAGMENT)
    }
}