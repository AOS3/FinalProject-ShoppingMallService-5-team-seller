package com.lion.judamie_seller.vo

import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.util.ProductState

class ProductVO {
    var productName	= ""
    var productCategory = ""
    var productPrice = 0
    var productDiscountRate	= 0
    var productStock = 0
    var productDescription = ""
    var productMainImage = ""
    var productSubImage = mutableListOf<String>()
    var productState = 0
    var productRegisterDate	= ""
    var productTimeStamp = 0L
    var productReview = mutableListOf<String>()

    fun toProductModel(productDocumentId:String) : ProductModel {
        val productModel = ProductModel()

        productModel.productDocumentId = productDocumentId
        productModel.productName = productName
        productModel.productCategory = productCategory
        productModel.productPrice = productPrice
        productModel.productDiscountRate = productDiscountRate
        productModel.productDescription = productDescription
        productModel.productStock = productStock
        productModel.productMainImage = productMainImage
        productModel.productSubImage = productSubImage
        productModel.productRegisterDate = productRegisterDate
        productModel.productTimeStamp = productTimeStamp
        productModel.productReview = productReview

        when (productState){
            ProductState.PRODUCT_STATE_NORMAL.number -> productModel.productState = ProductState.PRODUCT_STATE_NORMAL
            ProductState.PRODUCT_STATE_DELETE.number -> productModel.productState = ProductState.PRODUCT_STATE_DELETE
        }

        return productModel
    }
}