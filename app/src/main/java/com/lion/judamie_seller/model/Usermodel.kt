package com.lion.judamie_seller.model

import com.lion.judamie_seller.util.UserState
import com.lion.judamie_seller.vo.UserVO

class UserModel {
    // 문서 ID
    var sellerDocumentId = ""
    // 아이디
    var sellerId = ""
    // 비밀번호
    var sellerPw = ""

    var sellerPhoneNumber = ""

    var storeName = ""
    // 자동 로그인 토큰
    var sellerAutoLoginToken = ""

    var sellerProducts = mutableListOf<String>()
    // 시간
    var sellerTimeStamp = 0L
    // 상태값
    var sellerState = UserState.USER_STATE_NORMAL

    fun toUserVO() : UserVO {
        val userVO = UserVO()
        userVO.sellerId = sellerId
        userVO.sellerPw = sellerPw
        userVO.storeName = storeName
        userVO.sellerPhoneNumber = sellerPhoneNumber
        userVO.sellerProducts = sellerProducts
        userVO.sellerAutoLoginToken = sellerAutoLoginToken
        userVO.sellerTimeStamp = sellerTimeStamp
        userVO.sellerState = sellerState.number

        return userVO
    }
}