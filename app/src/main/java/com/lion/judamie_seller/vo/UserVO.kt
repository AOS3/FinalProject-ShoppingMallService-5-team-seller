package com.lion.judamie_seller.vo

import com.lion.judamie_seller.model.UserModel
import com.lion.judamie_seller.util.UserState

class UserVO(){
    // 아이디
    var userId = ""
    // 비밀번호
    var userPw = ""
    // 자동 로그인 토큰
    var userAutoLoginToken = ""

    // 시간
    var userTimeStamp:Long = 0
    // 상태값
    var userState = 0

    fun toUserModel(userDocumentId:String) : UserModel{
        val userModel = UserModel()

        userModel.userDocumentId = userDocumentId
        userModel.userId = userId
        userModel.userPw = userPw
        userModel.userAutoLoginToken = userAutoLoginToken
        userModel.userTimeStamp = userTimeStamp

        when(userState){
            UserState.USER_STATE_NORMAL.number -> userModel.userState = UserState.USER_STATE_NORMAL
            UserState.USER_STATE_SIGNOUT.number -> userModel.userState = UserState.USER_STATE_SIGNOUT
        }

        return userModel
    }
}