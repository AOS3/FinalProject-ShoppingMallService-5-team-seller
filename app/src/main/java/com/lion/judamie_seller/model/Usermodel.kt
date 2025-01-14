package com.lion.judamie_seller.model

import com.lion.judamie_seller.util.UserState
import com.lion.judamie_seller.vo.UserVO

class UserModel {
    // 문서 ID
    var userDocumentId = ""
    // 아이디
    var userId = ""
    // 비밀번호
    var userPw = ""
    // 자동 로그인 토큰
    var userAutoLoginToken = ""
    // 닉네임
    var userNickName = ""
    // 시간
    var userTimeStamp = 0L
    // 상태값
    var userState = UserState.USER_STATE_NORMAL

    fun toUserVO() : UserVO {
        val userVO = UserVO()
        userVO.userId = userId
        userVO.userPw = userPw
        userVO.userAutoLoginToken = userAutoLoginToken
        userVO.userTimeStamp = userTimeStamp
        userVO.userState = userState.number

        return userVO
    }
}