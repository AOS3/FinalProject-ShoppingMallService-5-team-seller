package com.lion.judamie_seller.vo

import com.lion.judamie_seller.model.CustomerModel
import com.lion.judamie_seller.util.CustomerState

// 고객 VO
class CustomerVO {
    // 유저 아이디
    var userId = ""
    // 유저 비밀번호
    var userPassword = ""
    // 유저 이름
    var userName = ""
    // 유저 전화번호
    var userPhoneNumber = ""
    // 찜 목록 (productDocumentID 리스트)
    var userWishList = mutableListOf<String>()
    // 장바구니 목록 (productDocumentID 리스트)
    var userCartList = mutableListOf<String>()
    // 유저가 지정한 픽업지
    var userPickupLoc: String = ""
    // 알림 설정 여부
    var userNotificationAllow: Boolean = false
    // 자동 로그인 토큰
    var userAutoLoginToken = ""
    // 탈퇴 여부 (기본: 1, 탈퇴: 2)
    // Values.UserState
    var userState = 0
    // 유저가 가진 쿠폰 리스트 (couponDocumentID)
    var userCoupons = mutableListOf<String>()
    // 데이터가 저장된 시간 (TimeStamp, nano)
    var userTimeStamp = 0L

    fun toCustomerModel(userDocumentId:String) : CustomerModel {
        val customerModel = CustomerModel()

        customerModel.userDocumentID = userDocumentId
        customerModel.userId = userId
        customerModel.userPassword = userPassword
        customerModel.userAutoLoginToken = userAutoLoginToken
        customerModel.userName = userName
        customerModel.userTimeStamp = userTimeStamp
        customerModel.userNotificationAllow = userNotificationAllow
        customerModel.userPhoneNumber = userPhoneNumber

        when(userState){
            CustomerState.USER_STATE_NORMAL.number -> customerModel.userState = CustomerState.USER_STATE_NORMAL
            CustomerState.USER_STATE_SIGN_OUT.number -> customerModel.userState = CustomerState.USER_STATE_SIGN_OUT
        }

        customerModel.userWishList = userWishList.toMutableList()
        customerModel.userCartList = userCartList.toMutableList()
        customerModel.userCoupons = userCoupons.toMutableList()
        customerModel.userPickupLoc = userPickupLoc


        return customerModel
    }
}

