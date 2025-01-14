package com.lion.judamie_seller.service

import com.lion.judamie_seller.model.UserModel
import com.lion.judamie_seller.repository.UserRepository
import com.lion.judamie_seller.vo.UserVO

class UserService {
    companion object {
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
    }
}