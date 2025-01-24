package com.lion.judamie_seller.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lion.judamie_seller.vo.UserVO
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object {

        // 사용자 정보를 추가하는 메서드
        fun addUserData(userVO: UserVO){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("SellerData")
            collectionReference.add(userVO)
        }

        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : MutableMap<String, *>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("SellerData")
            val result = collectionReference.whereEqualTo("sellerId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)

            val userMap = mutableMapOf(
                "user_document_id" to result.documents[0].id,
                "user_vo" to userVoList[0]
            )
            return userMap
        }

        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken: String): Map<String, *>? {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("SellerData")
            val resultList =
                collectionReference.whereEqualTo("sellerAutoLoginToken", loginToken).get().await()
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

        // 사용자 아이디를 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserId(userId:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("SellerData")
            val result = collectionReference.whereEqualTo("sellerId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }

        // 사용자 이름을 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserName(userName:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("SellerData")
            val result = collectionReference.whereEqualTo("sellerName", userName).get().await()
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }

        // 자동 로그인 토큰값 갱신 메서드
        suspend fun updateUserAutoLoginToken(userDocumentId:String, newToken:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("SellerData")
            val documentReference = collectionReference.document(userDocumentId)
            val tokenMap = mapOf(
                "userAutoLoginToken" to newToken
            )
            documentReference.update(tokenMap).await()
        }
    }
}