package com.lion.judamie_seller.model

import com.lion.judamie_seller.util.ProductState
import com.lion.judamie_seller.vo.OrderVO

class OrderModel {
    var orderDocumentID = ""
    var userDocumentID	= ""
    var sellerDocumentID = ""
    var orderTime = ""
    var productDocumentID = ""
    var productCategory = ""
    var productPrice = 0
    var productDiscountRate = 0
    var orderCount = 0
    var orderPriceAmount = 0
    var pickupLocAddressAll = ProductState.PRODUCT_STATE_NORMAL
    var deliveryState	= ""
    var orderTimeStamp = 0L
    var orderState = 0

    fun toOrderModel() : OrderVO {
        val orderVO = OrderVO()
        orderVO.userDocumentID = userDocumentID
        orderVO.sellerDocumentID = sellerDocumentID
        orderVO.orderTime = orderTime
        orderVO.productDocumentID = productDocumentID
        orderVO.productCategory = productCategory
        orderVO.productPrice = productPrice
        orderVO.productDiscountRate = productDiscountRate
        orderVO.orderCount = orderCount
        orderVO.orderPriceAmount = orderPriceAmount
        orderVO.pickupLocAddressAll = pickupLocAddressAll
        orderVO.deliveryState = deliveryState
        orderVO.orderTimeStamp = orderTimeStamp
        orderVO.orderState = orderState
        return orderVO
    }
}