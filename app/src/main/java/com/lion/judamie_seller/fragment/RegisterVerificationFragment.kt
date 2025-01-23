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
import com.lion.judamie_seller.model.UserModel
import com.lion.judamie_seller.service.UserService
import com.lion.judamie_seller.util.UserState
import com.lion.judamie_seller.viewmodel.RegisterVerificationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class RegisterVerificationFragment : Fragment() {
    lateinit var fragmentRegisterVerificationBinding: FragmentRegisterVerificationBinding
    lateinit var userActivity: UserActivity


    // 번들로 전달된 데이터를 담을 변수
    lateinit var userId:String
    lateinit var userPw:String
    lateinit var storeName:String

    // 닉네임 중복 확인을 했는지 확인하기 위한 변수
    var isCheckVerificationNumber = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRegisterVerificationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_verification, container, false)
        fragmentRegisterVerificationBinding.registerVerificationViewModel = RegisterVerificationViewModel(this@RegisterVerificationFragment)
        fragmentRegisterVerificationBinding.lifecycleOwner = this@RegisterVerificationFragment

        userActivity = activity as UserActivity

        gettingArguments()

        return fragmentRegisterVerificationBinding.root
    }
    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        userActivity.removeFragment(UserFragmentName.USER_REGISTER_VERIFICATION_FRAGMENT)
    }

    // 번들에 담겨져있는 데이터를 변수에 담아준다.
    fun gettingArguments(){
        userId = arguments?.getString("sellerId")!!
        userPw = arguments?.getString("sellerPw")!!
        storeName = arguments?.getString("sellerName")!!
    }

    // 가입 완료 처리 메서드
    fun proUserJoin(){
        fragmentRegisterVerificationBinding.apply {
            // 입력 검사
            if(registerVerificationViewModel?.textFieldUserRegisterPhoneNumberEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("휴대폰 번호 입력", "휴대폰 번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserPhoneNumber.editText!!)
                }
                return
            }
            if(registerVerificationViewModel?.textFieldUserRegisterVerificationNumberEditTextText?.value?.isEmpty()!!){
                userActivity.showMessageDialog("인증번호 입력", "인증번호를 입력해주세요", "확인"){
                    userActivity.showSoftInput(textFieldUserVerificationNumber.editText!!)
                }
                return
            }

            if (!isCheckVerificationNumber) {

            }

            // 저장할 데이터를 추출한다.
            val userPhoneNum = registerVerificationViewModel?.textFieldUserRegisterPhoneNumberEditTextText?.value!!
            var userTimeStamp = System.nanoTime()
            var userState = UserState.USER_STATE_NORMAL

            val userModel = UserModel().also {
                it.sellerId = userId
                it.sellerPw = userPw
                it.sellerName = storeName
                it.sellerState = userState
                it.sellerPhoneNumber = userPhoneNum
                it.sellerTimeStamp = userTimeStamp
            }

            // 저장한다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    // 서비스의 저장 메서드를 호출한다.
                    UserService.addUserData(userModel)
                }
                work1.join()
                userActivity.showMessageDialog("가입 완료", "가입이 완료되었습니다\n로그인해주세요", "확인"){
                    userActivity.removeFragment(UserFragmentName.USER_REGISTER_FRAGMENT)
                }
            }
        }
    }
    fun proVerificationConfirm(){
        isCheckVerificationNumber = true
    }
}