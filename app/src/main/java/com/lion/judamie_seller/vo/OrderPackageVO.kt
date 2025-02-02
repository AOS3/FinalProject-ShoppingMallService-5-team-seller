package com.lion.judamie_seller.vo

import com.lion.judamie_seller.model.OrderPackageModel
import com.lion.judamie_seller.util.OrderPackageState


class OrderPackageVO {
    // 주문 목록 (Order 문서 ID 리스트)
    var orderDataList = mutableListOf<String>()
    // 주문자 문서 ID
    var orderOwnerId = ""
    // 주문 패키지 상태값
    var orderPackageState = 0
    // 주문 패키지 데이터 생성 시간
    var orderPackageDataTimeStamp = 0L
    // 관리자 -> 판매자 입금 시간
    var orderTransactionTime = 0L
    // 주문자 픽업 상태
    var orderPickupState:Boolean = false
    // 관리자 -> 판매자 주문 입금 완료 상태
    var orderDepositState:Boolean = false

    fun toOrderPackageModel(orderPackageDocumentId:String) : OrderPackageModel {
        val orderPackageModel = OrderPackageModel()

        orderPackageModel.orderPackageDocumentId = orderPackageDocumentId
        orderPackageModel.orderDataList = ArrayList(orderDataList)
        orderPackageModel.orderOwnerId = orderOwnerId
        orderPackageModel.orderPackageDataTimeStamp = orderPackageDataTimeStamp
        orderPackageModel.orderTransactionTime = orderTransactionTime
        orderPackageModel.orderPickupState = orderPickupState
        orderPackageModel.orderDepositState = orderDepositState

        when(orderPackageState) {
            OrderPackageState.ORDER_PACKAGE_STATE_ENABLE.num -> orderPackageModel.orderPackageState = OrderPackageState.ORDER_PACKAGE_STATE_ENABLE
            OrderPackageState.ORDER_PACKAGE_STATE_DISABLE.num -> orderPackageModel.orderPackageState = OrderPackageState.ORDER_PACKAGE_STATE_ENABLE
        }

        return orderPackageModel
    }
}