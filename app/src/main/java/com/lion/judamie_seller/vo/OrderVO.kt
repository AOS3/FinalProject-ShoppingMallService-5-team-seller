package com.lion.judamie_seller.vo

import com.lion.judamie_seller.model.OrderModel
import com.lion.judamie_seller.util.ProductState

class OrderVO {
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

    fun toOrderModel(orderDocumentID:String) : OrderModel {
        val orderModel = OrderModel()
        orderModel.userDocumentID = userDocumentID
        orderModel.sellerDocumentID = sellerDocumentID
        orderModel.orderTime = orderTime
        orderModel.productDocumentID = productDocumentID
        orderModel.productCategory = productCategory
        orderModel.productPrice = productPrice
        orderModel.productDiscountRate = productDiscountRate
        orderModel.orderCount = orderCount
        orderModel.orderPriceAmount = orderPriceAmount
        orderModel.pickupLocAddressAll = pickupLocAddressAll
        orderModel.deliveryState = deliveryState
        orderModel.orderTimeStamp = orderTimeStamp
        orderModel.orderState = orderState
        return orderModel
    }
}