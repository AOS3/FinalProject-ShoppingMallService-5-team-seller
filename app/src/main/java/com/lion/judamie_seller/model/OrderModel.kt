package com.lion.judamie_seller.model

import com.lion.judamie_seller.vo.OrderVO

class OrderModel {
    var orderDocumentId = ""
    var userDocumentId	= ""
    var sellerDocumentId = ""
    var orderTime = 0L
    var productDocumentId = ""
    var productPrice = 0
    var productDiscountRate = 0
    var orderCount = 0
    var orderPriceAmount = 0.0
    var pickupLocDocumentId = ""
    var orderTimeStamp = 0L
    var orderState = 0
    var orderTransactionTime = 0L

    fun toOrderVO() : OrderVO {
        val orderVO = OrderVO()
        orderVO.userDocumentId = userDocumentId
        orderVO.sellerDocumentId = sellerDocumentId
        orderVO.orderTime = orderTime
        orderVO.productDocumentId = productDocumentId
        orderVO.productPrice = productPrice
        orderVO.productDiscountRate = productDiscountRate
        orderVO.orderCount = orderCount
        orderVO.orderPriceAmount = orderPriceAmount
        orderVO.pickupLocDocumentId = pickupLocDocumentId
        orderVO.orderTimeStamp = orderTimeStamp
        orderVO.orderState = orderState
        orderVO.orderTransactionTime = orderTransactionTime
        return orderVO
    }
}