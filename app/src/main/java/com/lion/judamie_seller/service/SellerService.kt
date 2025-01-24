package com.lion.judamie_seller.service

import android.net.Uri
import android.util.Log
import com.lion.judamie_seller.model.OrderModel
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.repository.SellerRepository
import com.lion.judamie_seller.repository.SellerRepository.Companion.gettingImage
import com.lion.judamie_seller.repository.UserRepository
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.vo.OrderVO
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
             Log.d("test100", " ${resultList.size} 카테고리 항목: ${productType.str}")

            return productList
        }

        // 상품 목록을 가져오는 메서드
        suspend fun gettingOrderList(productType: ProductType) : MutableList<OrderModel>{
            // 글정보를 가져온다.
            val orderList = mutableListOf<OrderModel>()
            val resultList = SellerRepository.gettingOrderList(productType)

            resultList.forEach {
                val orderVO = it["orderVO"] as OrderVO
                val documentId = it["documentId"] as String
                val orderModel = orderVO.toOrderModel(documentId)
                orderList.add(orderModel)
            }

            return orderList
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

        suspend fun removeImageFiles(imageFileNames: List<String>, productName: String) {
            // productSubImages의 크기만큼 반복하면서 삭제할 파일 이름을 생성
            val filteredFiles = imageFileNames.mapIndexed { index, _ ->
                "sub_image_${productName}_$index"
            }

            // 생성된 파일 이름 목록에서 실제 파일 이름을 확인하고 삭제
            filteredFiles.forEach { imageFileName ->
                if (imageFileNames.contains(imageFileName)) {
                    SellerRepository.removeImageFile(imageFileName)
                }
            }
        }

        // 서버에서 글을 삭제한다.
        suspend fun deleteProductData(productDocumentId:String){
            SellerRepository.deleteProductData(productDocumentId)
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectProductDataOneById(documentId:String) : ProductModel{
            // 글 데이터를 가져온다.
            val productVO = SellerRepository.selectProductDataOneById(documentId)
            // productModel객체를 생성한다.
            val productModel = productVO.toProductModel(documentId)

            return productModel
        }

        // 이미지 데이터를 가져온다.
        suspend fun gettingMainImage(imageFileName:String) : Uri {
            val imageUri = SellerRepository.gettingImage(imageFileName)
            return imageUri
        }

        suspend fun gettingSubImages(imageFileNames: List<String>): List<Uri> {
            return imageFileNames.map { imageFileName ->
                gettingImage(imageFileName) // 개별 이미지를 처리
            }
        }
    }
}