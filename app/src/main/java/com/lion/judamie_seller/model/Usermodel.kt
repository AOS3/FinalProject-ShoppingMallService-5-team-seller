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

    var sellerName = ""
    // 자동 로그인 토큰
    var sellerAutoLoginToken = ""
    // 시간
    var sellerTimeStamp = 0L

    var sellerPhoneNumber = ""

    var sellerProducts = mutableListOf<String>()
    // 상태값
    var sellerState = UserState.USER_STATE_NORMAL

    fun toUserVO() : UserVO {
        val sellerVO = UserVO()
        sellerVO.sellerId = sellerId
        sellerVO.sellerPw = sellerPw
        sellerVO.sellerName = sellerName
        sellerVO.sellerAutoLoginToken = sellerAutoLoginToken
        sellerVO.sellerTimeStamp = sellerTimeStamp
        sellerVO.sellerState = sellerState.number
        sellerVO.sellerPhoneNumber = sellerPhoneNumber
        sellerVO.sellerProducts = sellerProducts


        return sellerVO
    }
}