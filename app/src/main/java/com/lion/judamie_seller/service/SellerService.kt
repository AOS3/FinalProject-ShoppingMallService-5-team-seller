package com.lion.judamie_seller.service

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1.StructuredQuery.Order
import com.lion.judamie_seller.model.CustomerModel
import com.lion.judamie_seller.model.OrderModel
import com.lion.judamie_seller.model.OrderPackageModel
import com.lion.judamie_seller.model.PickupLocationModel
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.repository.SellerRepository
import com.lion.judamie_seller.repository.SellerRepository.Companion.gettingImage
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.vo.CustomerVO
import com.lion.judamie_seller.vo.OrderPackageVO
import com.lion.judamie_seller.vo.OrderVO
import com.lion.judamie_seller.vo.ProductVO
import com.lion.judamie_seller.vo.UserVO
import kotlinx.coroutines.tasks.await

class SellerService {
    companion object {
        suspend fun uploadImage(sourceFilePath:String, serverFilePath:String, isMainImage: Boolean){
            SellerRepository.uploadImage(sourceFilePath, serverFilePath, isMainImage)
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

        // 주문 목록을 가져오는 메서드
        suspend fun gettingOrderPackageList() : MutableList<OrderPackageModel>{
            // 글정보를 가져온다.
            val orderPackageList = mutableListOf<OrderPackageModel>()
            val resultList = SellerRepository.gettingOrderPackageList()

            resultList.forEach {
                val orderPackageVO = it["orderPackageVO"] as OrderPackageVO
                val documentId = it["documentId"] as String
                val orderPackageModel = orderPackageVO.toOrderPackageModel(documentId)
                orderPackageList.add(orderPackageModel)
            }

            return orderPackageList
        }

        // 주문 목록을 가져오는 메서드
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

        // 주문 목록을 가져오는 메서드
        suspend fun gettingOrderList(orderDocumentId: String) : MutableList<OrderModel>{
            // 글정보를 가져온다.
            val orderList = mutableListOf<OrderModel>()
            val resultList = SellerRepository.getOrderByDocumentId(orderDocumentId)

            resultList.forEach {
                val orderVO = it["orderVO"] as OrderVO
                val documentId = it["documentId"] as String
                val orderModel = orderVO.toOrderModel(documentId)
                orderList.add(orderModel)
            }

            return orderList
        }

        // 주문 목록을 가져오는 메서드
        suspend fun gettingOrderByDocumentId(orderDocumentId: String) : MutableList<OrderModel>{
            // 글정보를 가져온다.
            val orderList = mutableListOf<OrderModel>()
            val resultList = SellerRepository.getOrderByDocumentId(orderDocumentId)

            resultList.forEach {
                val orderVO = it["orderVO"] as OrderVO
                val documentId = it["documentId"] as String
                val orderModel = orderVO.toOrderModel(documentId)
                orderList.add(orderModel)
            }

            return orderList
        }


        // 주문 목록을 가져오는 메서드
        suspend fun gettingProductByDocumentId(productDocumentId: String) : MutableList<ProductModel>{
            // 글정보를 가져온다.
            val productList = mutableListOf<ProductModel>()
            val resultList = SellerRepository.getProductByDocumentId(productDocumentId)

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

        suspend fun updateOderStateData(orderModel: OrderModel){
            // vo 객체에 담아준다.
            val orderVO = orderModel.toOrderVO()
            // 수정하는 메서드를 호출한다.
            SellerRepository.updateOrderStateData(orderVO, orderModel.orderDocumentId)
        }

        // 서버에서 이미지 파일을 삭제한다.
        suspend fun removeImageFile(imageFileName:String){
            SellerRepository.removeImageFile(imageFileName)
        }

        suspend fun removeImageFiles(imageFileNames: List<String>) {
            SellerRepository.removeImageFiles(imageFileNames)
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

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectOrderDataOneById(documentId:String) : OrderModel{
            // 글 데이터를 가져온다.
            val orderVO = SellerRepository.selectOrderDataOneById(documentId)
            // productModel객체를 생성한다.
            val orderModel = orderVO.toOrderModel(documentId)

            return orderModel
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectCustomerDataOneById(documentId:String) : CustomerModel{
            // 글 데이터를 가져온다.
            val customerVO = SellerRepository.selectCustomerDataOneById(documentId)
            // productModel객체를 생성한다.
            val customerModel = customerVO.toCustomerModel(documentId)

            return customerModel
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectPickupLocDataOneById(documentId:String) : PickupLocationModel{
            // 글 데이터를 가져온다.
            val pickupLocVO = SellerRepository.selectPickupLocDataOneById(documentId)
            // productModel객체를 생성한다.
            val pickupLocModel = pickupLocVO.toPickupLocationModel(documentId)

            return pickupLocModel
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectOrderPackageDataOneById(documentId:String) : OrderPackageModel{
            // 글 데이터를 가져온다.
            val orderPackageVO = SellerRepository.selectOrderPackageDataOneById(documentId)
            // productModel객체를 생성한다.
            val orderPackageModel = orderPackageVO.toOrderPackageModel(documentId)

            return orderPackageModel
        }

        // 이미지 데이터를 가져온다.
        suspend fun gettingMainImage(imageFileName:String) : Uri {
            val imageUri = SellerRepository.gettingImage(imageFileName, true)
            return imageUri
        }

        suspend fun gettingSubImages(imageFileNames: List<String>): List<Uri> {
            return imageFileNames.map { imageFileName ->
                gettingImage(imageFileName, false) // 개별 이미지를 처리
            }
        }
    }
}