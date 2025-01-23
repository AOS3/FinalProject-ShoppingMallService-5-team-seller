package com.lion.judamie_seller.vo

import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.model.UserModel
import com.lion.judamie_seller.util.UserState

class UserVO(){
    // 아이디
    var sellerId = ""
    // 비밀번호
    var sellerPw = ""
    // 자동 로그인 토큰
    var sellerAutoLoginToken = ""

    var sellerName = ""
    // 시간
    var sellerTimeStamp:Long = 0
    // 상태값
    var sellerState = 0

    var sellerPhoneNumber = ""

    var sellerProducts = mutableListOf<String>()

    fun toUserModel(userDocumentId:String) : UserModel{
        val sellerModel = UserModel()

        sellerModel.sellerDocumentId = userDocumentId
        sellerModel.sellerId = sellerId
        sellerModel.sellerPw = sellerPw
        sellerModel.sellerName = sellerName
        sellerModel.sellerAutoLoginToken = sellerAutoLoginToken
        sellerModel.sellerTimeStamp = sellerTimeStamp
        sellerModel.sellerProducts = sellerProducts
        sellerModel.sellerPhoneNumber = sellerPhoneNumber


        when(sellerState){
            UserState.USER_STATE_NORMAL.number -> sellerModel.sellerState = UserState.USER_STATE_NORMAL
            UserState.USER_STATE_SIGNOUT.number -> sellerModel.sellerState = UserState.USER_STATE_SIGNOUT
        }

        return sellerModel
    }
}