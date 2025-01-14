package com.lion.judamie_seller.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lion.judamie_seller.vo.UserVO
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object {
        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken: String): Map<String, *>? {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val resultList =
                collectionReference.whereEqualTo("userAutoLoginToken", loginToken).get().await()
            val userVOList = resultList.toObjects(UserVO::class.java)
            if (userVOList.isEmpty()) {
                return null
            } else {
                val userDocumentId = resultList.documents[0].id
                val returnMap = mapOf(
                    "userDocumentId" to userDocumentId,
                    "userVO" to userVOList[0]
                )
                return returnMap
            }
        }
    }
}