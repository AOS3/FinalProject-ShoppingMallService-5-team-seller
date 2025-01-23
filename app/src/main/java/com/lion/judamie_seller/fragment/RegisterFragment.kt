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

    // 아이디 중복 확인 검사를 했는지 확인하는 변수
    var isCheckRegisterUserIdExist = false

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
            val userId = registerViewModel?.textFieldUserRegisterIDEditTextText?.value!!
            val userPw = registerViewModel?.textFieldUserRegisterPWEditTextText?.value!!
            val storeName = registerViewModel?.textFieldUserRegisterStoreNameEditTextText?.value!!

            // 데이터를 담는다.
            val dataBundle = Bundle()
            dataBundle.putString("sellerId", userId)
            dataBundle.putString("sellerPw", userPw)
            dataBundle.putString("sellerName", storeName)
            userActivity.replaceFragment(
                UserFragmentName.USER_REGISTER_VERIFICATION_FRAGMENT,
                true,
                true,
                dataBundle
            )
        }
    }

    // 아이디 중복 확인처리
    fun checkRegisterUserId(){
        // 사용자가 입력한 아이디
        val userId = fragmentRegisterViewBinding.registerViewModel?.textFieldUserRegisterIDEditTextText?.value!!
        // 입력한 것이 없다면
        if(userId.isEmpty()){
            userActivity.showMessageDialog("아이디 입력", "아이디를 입력해주세요", "확인"){
                userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterID)
            }
            return
        }
        // 사용할 수 있는 아이디인지 검사한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                UserService.checkRegisterUserId(userId)
            }
            val chk = work1.await()

            // 사용할 수 있는 아이디인지 여부값을 담아준다.
            isCheckRegisterUserIdExist = chk

            if(chk){
                userActivity.showMessageDialog("중복확인", "사용할 수 있는 아이디 입니다", "확인"){

                }
            } else{
                userActivity.showMessageDialog("중복확인", "이미 존재하는 아이디 입니다", "확인"){
                    fragmentRegisterViewBinding.registerViewModel?.textFieldUserRegisterIDEditTextText?.value = ""
                    userActivity.showSoftInput(fragmentRegisterViewBinding.textFieldUserRegisterID)

                }
            }
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        userActivity.removeFragment(UserFragmentName.USER_REGISTER_FRAGMENT)
    }
}
