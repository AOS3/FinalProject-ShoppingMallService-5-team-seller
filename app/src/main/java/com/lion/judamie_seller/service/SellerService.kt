package com.lion.judamie_seller.service

import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.repository.SellerRepository
import com.lion.judamie_seller.repository.UserRepository
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.vo.ProductVO
import com.lion.judamie_seller.vo.UserVO

class SellerService {
    companion object {
        suspend fun uploadImage(sourceFilePath:String, serverFilePath:String){
            SellerRepository.uploadImage(sourceFilePath, serverFilePath)
        }

        suspend fun addProductData(productModel: ProductModel): String {
            // VO 객체를 생성한다.
            val productVO = productModel.toProductVO()
            // 저장한다.
            val documentId = SellerRepository.addProductData(productVO)
            return documentId
        }

        // 상품 목록을 가져오는 메서드
        suspend fun gettingProductList(productType: ProductType) : MutableList<ProductModel>{
            // 글정보를 가져온다.
            val productList = mutableListOf<ProductModel>()
            val resultList = SellerRepository.gettingProductList(productType)

            resultList.forEach {
                val productVO = it["productVO"] as ProductVO
                val documentId = it["documentId"] as String
                val productModel = productVO.toProductModel(documentId)
                productList.add(productModel)
            }

            return productList
        }

        // 글정보를 수정하는 메서드
        suspend fun updateProductData(productModel: ProductModel){
            // vo 객체에 담아준다.
            val productVO = productModel.toProductVO()
            // 수정하는 메서드를 호출한다.
            SellerRepository.updateProductData(productVO, productModel.productDocumentId)
        }

        // 서버에서 이미지 파일을 삭제한다.
        suspend fun removeImageFile(imageFileName:String){
            SellerRepository.removeImageFile(imageFileName)
        }

        // 서버에서 글을 삭제한다.
        suspend fun deleteProductData(productDocumentId:String){
            SellerRepository.deleteProductData(productDocumentId)
        }
    }
}