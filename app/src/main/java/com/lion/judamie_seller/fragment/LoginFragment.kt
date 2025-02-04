package com.lion.judamie_seller.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.UserActivity
import com.lion.judamie_seller.UserFragmentName
import com.lion.judamie_seller.databinding.FragmentLoginBinding
import com.lion.judamie_seller.service.UserService
import com.lion.judamie_seller.util.LoginResult
import com.lion.judamie_seller.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var userActivity: UserActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        fragmentLoginBinding.loginViewModel = LoginViewModel(this@LoginFragment)
        fragmentLoginBinding.lifecycleOwner = this@LoginFragment

        userActivity = activity as UserActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()

        return fragmentLoginBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentLoginBinding.loginViewModel?.apply {
            toolbarUserLoginTitle.value = "로그인"
        }
    }

    // 회원 가입 화면으로 이동시키는 메서드
    fun moveToUserRegister() {
        userActivity.replaceFragment(UserFragmentName.USER_REGISTER_FRAGMENT, true, true, null)
    }

    // 로그인 처리 메서드
    fun proLogin() {
        fragmentLoginBinding.apply {
            var hasError = false // 에러 상태 플래그

            // 아이디 입력 검사
            if (loginViewModel?.textFieldUserLoginIdEditTextText?.value?.isEmpty() == true) {
                textFieldUserLoginId.error = "아이디를 입력해주세요"
                hasError = true // 에러 플래그 설정
            } else {
                textFieldUserLoginId.error = null
                hasError = false
            }

            // 비밀번호 입력 검사
            if (loginViewModel?.textFieldUserLoginPwEditTextText?.value?.isEmpty() == true) {
                textFieldUserLoginPw.error = "비밀번호를 입력해주세요"
                hasError = true
            } else {
                textFieldUserLoginPw.error = null
                hasError = false
            }

            // 에러가 있을 경우 로그인 성공 처리를 중단
            if (hasError) {
                return
            } else {

            }

            // 사용자가 입력한 아이디와 비밀번호
            val loginUserId = loginViewModel?.textFieldUserLoginIdEditTextText?.value!!
            val loginUserPw = loginViewModel?.textFieldUserLoginPwEditTextText?.value!!
            Log.d("userDocumentId5", loginUserPw)
            Log.d("userDocumentId6", loginUserId)
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    UserService.checkLogin(loginUserId, loginUserPw)
                }
                // 로그인 결과를 가져온다.
                val loginResult = work1.await()

                // 로그인 결과로 분기
                when (loginResult) {
                    // 등록되지 않은 아이디
                    LoginResult.LOGIN_RESULT_ID_NOT_EXIST -> {
                        textFieldUserLoginId.error = "등록되지않은 아이디입니다."
                        userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                    }
                    // 비밀번호 틀림
                    LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT -> {
                        textFieldUserLoginPw.error = "잘못된 비밀번호입니다."
                        userActivity.showSoftInput(textFieldUserLoginPw.editText!!)
                    }
                    // 탈퇴 회원
                    LoginResult.LOGIN_RESULT_SIGNOUT_MEMBER -> {
                        userActivity.showMessageDialog("로그인 실패", "탈퇴한 회원 입니다", "확인") {
                            loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                            loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                            userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                        }
                    }
                    // 로그인 성공
                    LoginResult.LOGIN_RESULT_SUCCESS -> {
                        // 로그인 사용자 정보를 가져온다.
                        val work2 = async(Dispatchers.IO) {
                            UserService.selectUserDataByUserIdOne(loginUserId)
                        }
                        val loginUserModel = work2.await()

                        // 자동 로그인 체크 시,
                        if (loginViewModel?.checkBoxUserLoginAutoChecked?.value!!) {
                            CoroutineScope(Dispatchers.Main).launch {
                                val work3 = async(Dispatchers.IO) {
                                    UserService.updateUserAutoLoginToken(
                                        userActivity,
                                        loginUserModel.sellerDocumentId
                                    )
                                }
                                work3.join()
                                Log.d("test100", "자동 로그인 토큰 업데이트 완료")
                            }
                        }

                        // LoginActivity 종료, ShopActivity 실행
                        val shopIntent = Intent(userActivity, SellerActivity::class.java)
                        shopIntent.putExtra("sellerDocumentId", loginUserModel.sellerDocumentId)
                        shopIntent.putExtra("sellerStoreName", loginUserModel.storeName)
                        shopIntent.putExtra("sellerPw", loginUserPw)
                        Log.d("userDocumentId4","${loginUserPw}")
                        startActivity(shopIntent)
                        userActivity.finish()
                    }
                }
            }
        }
    }
}