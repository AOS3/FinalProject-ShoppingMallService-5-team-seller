package com.lion.judamie_seller.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.UserFragmentName
import com.lion.judamie_seller.databinding.FragmentModifyInfoBinding
import com.lion.judamie_seller.model.UserModel
import com.lion.judamie_seller.service.UserService
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.util.UserState
import com.lion.judamie_seller.viewmodel.ModifyInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ModifyInfoFragment() : Fragment() {

    lateinit var fragmentModifyInfoBinding: FragmentModifyInfoBinding
    lateinit var sellerActivity: SellerActivity

    lateinit var auth: FirebaseAuth
    var verificationId: String = ""
    // 본인 인증 했는지 확인하는 변수
    var isVerification = false

    // 번들로 전달된 데이터를 담을 변수
    lateinit var sellerId:String
    lateinit var sellerPw:String
    lateinit var storeName:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentModifyInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_info, container, false)
        fragmentModifyInfoBinding.modifyInfoViewModel = ModifyInfoViewModel(this@ModifyInfoFragment)
        fragmentModifyInfoBinding.lifecycleOwner = this@ModifyInfoFragment

        sellerActivity = activity as SellerActivity

        auth = FirebaseAuth.getInstance()

        gettingArguments()

        return fragmentModifyInfoBinding.root
    }

    // 번들에 담겨져있는 데이터를 변수에 담아준다.
    fun gettingArguments(){
        sellerId = arguments?.getString("sellerDocumentId")!!
        sellerPw = arguments?.getString("sellerPw")!!
        storeName = arguments?.getString("sellerStoreName")!!
    }

    // 인증 코드 전송 메서드
    fun sendVerificationCode(phoneNumber: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(requireContext(), "인증 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@ModifyInfoFragment.verificationId = verificationId
                Toast.makeText(requireContext(), "인증 코드가 전송되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // Callbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // 인증 코드 확인 버튼
    fun verificationCheck() {
        val authCode = fragmentModifyInfoBinding.modifyInfoViewModel?.textFieldVerificationText?.value!!
        if (authCode.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationId, authCode)
            signInWithPhoneAuthCredential(credential)
        } else {
            fragmentModifyInfoBinding.textFieldVerificationCode.error = "인증코드를 입력해주세요"
        }
    }

    // 인증 코드 확인 메서드
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "인증 성공", Toast.LENGTH_SHORT).show()
                    isVerification = true

                } else {
                    fragmentModifyInfoBinding.textFieldVerificationCode.error = "인증코드가 일치하지 않습니다"
                }
            }
    }

    // 정보 수정 완료 처리 메서드
    fun proChange() {
        fragmentModifyInfoBinding.apply {
            if (isVerification) {
                // 저장할 데이터를 추출한다
                var sellerPhoneNumber =
                    modifyInfoViewModel?.textFieldPhoneNumber?.value!!
                var sellerTimeStamp = System.nanoTime()
                var sellerState = UserState.USER_STATE_NORMAL
                var newSellerPw = modifyInfoViewModel?.textNewPasswordText?.value!!

                val userModel = UserModel().also {
                    it.sellerId = sellerId
                    it.sellerPw = newSellerPw
                    it.storeName = storeName
                    it.sellerPhoneNumber = sellerPhoneNumber
                    it.sellerTimeStamp = sellerTimeStamp
                    it.sellerState = sellerState
                }

                // 서버에 저장한다
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO) {
                        // 서비스의 저장 메서드를 호출한다.
                        Log.d("userDocumentId1", "$userModel.sellerDocumentId, $sellerPw")
                        UserService.updateUserPwData(requireContext(), sellerId, newSellerPw)
                    }
                    work1.join()
                    sellerActivity.showMessageDialog("비밀 번호 변경 완료", "비밀번호가 변경되었습니다\n로그인해주세요", "확인") {
                        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_INFO)
                    }
                }
            } else {
                sellerActivity.showMessageDialog("가입 실패", "인증을 완료해주세요", "확인") {

                }
            }
        }
    }


    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_INFO)
    }
}