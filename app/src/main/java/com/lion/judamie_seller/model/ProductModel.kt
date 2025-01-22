package com.lion.judamie_seller.model

import com.lion.judamie_seller.util.ProductState
import com.lion.judamie_seller.vo.ProductVO

class ProductModel {
    var productDocumentIdD = ""
    var productDocumentId = ""
    var productName	= ""
    var productCategory = ""
    var productPrice = 0
    var productDiscountRate	= 0
    var productDescription = ""
    var productStock = 0
    var productMainImage = ""
    var productSubImage = mutableListOf<String>()
    var productState = ProductState.PRODUCT_STATE_NORMAL
    var productRegisterDate	= ""
    var productTimeStamp = 0L

    fun toProductVO() : ProductVO {
        val productVO = ProductVO()
        productVO.productName = productName
        productVO.productCategory = productCategory
        productVO.productPrice = productPrice
        productVO.productDiscountRate = productDiscountRate
        productVO.productDescription = productDescription
        productVO.productStock = productStock
        productVO.productMainImage = productMainImage
        productVO.productSubImage = productSubImage
        productVO.productState = productState.number
        productVO.productRegisterDate = productRegisterDate
        productVO.productTimeStamp = productTimeStamp

        return productVO
    }
}