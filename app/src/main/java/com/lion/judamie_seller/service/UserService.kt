package com.lion.judamie_seller.service

import android.content.Context
import androidx.core.content.edit
import com.lion.judamie_seller.model.UserModel
import com.lion.judamie_seller.repository.UserRepository
import com.lion.judamie_seller.util.LoginResult
import com.lion.judamie_seller.util.UserState
import com.lion.judamie_seller.vo.UserVO

class UserService {
    companion object {

        // 사용자 정보를 추가하는 메서드
        fun addUserData(userModel: UserModel){
            // 데이터를 VO에 담아준다.
            val userVO = userModel.toUserVO()
            // 저장하는 메서드를 호출한다.
            UserRepository.addUserData(userVO)
        }

        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : UserModel{
            val tempMap = UserRepository.selectUserDataByUserIdOne(userId)
            val loginUserVo = tempMap["user_vo"] as UserVO
            val loginUserDocumentId = tempMap["user_document_id"] as String

            val loginUserModel = loginUserVo.toUserModel(loginUserDocumentId)

            return loginUserModel
        }

        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken: String): UserModel? {
            val loginMap = UserRepository.selectUserDataByLoginToken(loginToken)
            if (loginMap == null) {
                return null
            } else {
                val userDocumentId = loginMap["userDocumentId"] as String
                val userVO = loginMap["userVO"] as UserVO

                val userModel = userVO.toUserModel(userDocumentId)
                return userModel
            }
        }

        // 가입하려는 아이디가 존재하는지 확인하는 메서드
        suspend fun checkJoinUserId(userId:String) : Boolean{
            // 아이디를 통해 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(userId)
            // 가져온 데이터가 있다면
            if(userVoList.isNotEmpty()){
                return false
            }
            // 가져온 데이터가 없다면
            else {
                return true
            }
        }

        // 가입하려는 이름이 존재하는지 확인하는 메서드
        suspend fun checkJoinUserName(userName:String) : Boolean{
            // 아이디를 통해 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserName(userName)
            // 가져온 데이터가 있다면
            if(userVoList.isNotEmpty()){
                return false
            }
            // 가져온 데이터가 없다면
            else {
                return true
            }
        }

        // 로그인 처리 메서드
        suspend fun checkLogin(loginUserId:String, loginUserPw:String) : LoginResult {
            // 로그인 결과
            var result = LoginResult.LOGIN_RESULT_SUCCESS

            // 입력한 아이디로 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(loginUserId)
            // 가져온 사용자 정보가 없다면
            if(userVoList.isEmpty()){
                result = LoginResult.LOGIN_RESULT_ID_NOT_EXIST
            } else {
                if(loginUserPw != userVoList[0].sellerPw){
                    // 비밀번호가 다르다면
                    result = LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT
                }
                // 탈퇴한 회원 이라면
                if(userVoList[0].sellerState == LoginResult.LOGIN_RESULT_SIGNOUT_MEMBER.number){
                    result = LoginResult.LOGIN_RESULT_SIGNOUT_MEMBER
                }
            }
            return result
        }

        // 자동 로그인 토큰값 갱신 메서드
        suspend fun updateUserAutoLoginToken(context: Context, userDocumentId:String){
            // 새로운 토큰 값 발행
            val newToken = "${userDocumentId}${System.nanoTime()}"
            // SharedPreference 에 저장
            val pref = context.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
            pref.edit {
                putString("token", newToken)
            }
            // 서버에 저장
            UserRepository.updateUserAutoLoginToken(userDocumentId, newToken)
        }
    }
}