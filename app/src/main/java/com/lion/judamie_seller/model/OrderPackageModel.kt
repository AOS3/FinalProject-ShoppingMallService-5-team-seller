package com.lion.judamie_seller.model

import com.lion.judamie_seller.util.OrderPackageState
import com.lion.judamie_seller.vo.OrderPackageVO


class OrderPackageModel {
    // 주문 패키지 문서 ID
    var orderPackageDocumentId = ""
    // 주문 목록 (Order 문서 ID 리스트)
    var orderDataList = mutableListOf<String>()
    // 주문자 문서 ID
    var orderOwnerId = ""
    // 주문 패키지 상태값
    var orderPackageState = OrderPackageState.ORDER_PACKAGE_STATE_ENABLE
    // 주문 패키지 데이터 생성 시간
    var orderPackageDataTimeStamp = 0L
    // 관리자 -> 판매자 입금 시간
    var orderTransactionTime = 0L
    // 주문자 픽업 상태
    var orderPickupState:Boolean = false
    // 관리자 -> 판매자 주문 입금 완료 상태
    var orderDepositState:Boolean = false

    fun toOrderPackageVO() : OrderPackageVO {
        val orderPackageVO = OrderPackageVO()

        orderPackageVO.orderDataList = ArrayList(orderDataList)
        orderPackageVO.orderOwnerId = orderOwnerId
        orderPackageVO.orderPackageDataTimeStamp = orderPackageDataTimeStamp
        orderPackageVO.orderTransactionTime = orderTransactionTime
        orderPackageVO.orderPickupState = orderPickupState
        orderPackageVO.orderDepositState = orderDepositState

        orderPackageVO.orderPackageState = orderPackageState.num

        return orderPackageVO
    }
}