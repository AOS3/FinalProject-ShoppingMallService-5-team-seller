package com.lion.judamie_seller.vo

import com.lion.judamie_seller.model.UserModel
import com.lion.judamie_seller.util.UserState

class UserVO(){
    // 아이디
    var sellerId = ""
    // 비밀번호
    var sellerPw = ""
    // 자동 로그인 토큰
    var sellerAutoLoginToken = ""

    var sellerPhoneNumber = ""

    var sellerProducts = mutableListOf<String>()

    var storeName = ""
    // 시간
    var sellerTimeStamp:Long = 0
    // 상태값
    var sellerState = 0

    fun toUserModel(sellerDocumentId:String) : UserModel{
        val userModel = UserModel()

        userModel.sellerDocumentId = sellerDocumentId
        userModel.sellerId = sellerId
        userModel.storeName = storeName
        userModel.sellerPhoneNumber = sellerPhoneNumber
        userModel.sellerProducts = sellerProducts
        userModel.sellerPw = sellerPw
        userModel.sellerAutoLoginToken = sellerAutoLoginToken
        userModel.sellerTimeStamp = sellerTimeStamp

        when(sellerState){
            UserState.USER_STATE_NORMAL.number -> userModel.sellerState = UserState.USER_STATE_NORMAL
            UserState.USER_STATE_SIGNOUT.number -> userModel.sellerState = UserState.USER_STATE_SIGNOUT
        }

        return userModel
    }
}