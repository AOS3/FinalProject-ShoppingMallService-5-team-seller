package com.lion.judamie_seller.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.lion.judamie_seller.model.CustomerModel
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.vo.CustomerVO
import com.lion.judamie_seller.vo.OrderPackageVO
import com.lion.judamie_seller.vo.OrderVO
import com.lion.judamie_seller.vo.PickupLocationVO
import com.lion.judamie_seller.vo.ProductVO
import kotlinx.coroutines.tasks.await
import java.io.File

class SellerRepository {
    companion object {
        // 이미지 데이터를 서버로 업로드 하는 메서드
        suspend fun uploadImage(sourceFilePath: String, serverFilePath: String, isMainImage: Boolean) {
            // 저장되어 있는 이미지의 경로
            val file = File(sourceFilePath)
            val fileUri = Uri.fromFile(file)
            // 업로드 한다.
            val firebaseStorage = FirebaseStorage.getInstance()
            val childReference = firebaseStorage.reference.child("image/$serverFilePath")
            childReference.putFile(fileUri).await()
        }

        // 글 데이터를 저장하는 메서드
        // 새롭게 추가된 문서의 id를 반환한다.
        suspend fun addProductData(productVO: ProductVO): String {
            val fireStore = FirebaseFirestore.getInstance()
            val collectionReference = fireStore.collection("ProductData")
            val documentReference = collectionReference.add(productVO).await()
            return documentReference.id
        }

        // 글 목록을 가져오는 메서드
        suspend fun gettingProductList(productType: ProductType) : MutableList<Map<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("ProductData")
            // 데이터를 가져온다.
            val result = if(productType == ProductType.PRODUCT_TYPE_ALL){
                collectionReference.orderBy("productTimeStamp", Query.Direction.DESCENDING).get().await()
            } else {
                collectionReference.whereEqualTo("productCategory", productType.str)
                    .orderBy("productTimeStamp", Query.Direction.DESCENDING).get().await()
            }
            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 문서의 id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "productVO" to it.toObject(ProductVO::class.java)
                )
                resultList.add(map)
            }
            return resultList
        }

        // 글 목록을 가져오는 메서드
        suspend fun gettingOrderList(productType: ProductType) : MutableList<Map<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderData")
            // 데이터를 가져온다.
            val result = if(productType == ProductType.PRODUCT_TYPE_ALL){
                collectionReference.orderBy("orderTimeStamp", Query.Direction.DESCENDING).get().await()
            } else {
                collectionReference.whereEqualTo("productCategory", productType.str)
                    .orderBy("orderTimeStamp", Query.Direction.DESCENDING).get().await()
            }
            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 문서의 id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "orderVO" to it.toObject(OrderVO::class.java)
                )
                resultList.add(map)
            }
            return resultList
        }


        // 글 목록을 가져오는 메서드
        suspend fun gettingOrderPackageList() : MutableList<Map<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderPackageData")
            // 데이터를 가져온다.
            val result = collectionReference.orderBy("orderPackageDataTimeStamp", Query.Direction.DESCENDING).get().await()
            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 문서의 id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "orderPackageVO" to it.toObject(OrderPackageVO::class.java)
                )
                resultList.add(map)
            }
            return resultList
        }

        // 글 목록을 가져오는 메서드
        suspend fun gettingCustomerList() : MutableList<Map<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            // 데이터를 가져온다.
            val result = collectionReference.orderBy("userTimeStamp", Query.Direction.DESCENDING).get().await()
            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 문서의 id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "CustomerVO" to it.toObject(CustomerVO::class.java)
                )
                resultList.add(map)
            }
            return resultList
        }

        // 글정보를 수정하는 메서드
        suspend fun updateProductData(productVO: ProductVO, productDocumentId:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("ProductData")
            val documentReference = collectionReference.document(productDocumentId)

            // 수정할 데이터를 맵에 담는다
            // 데이터의 이름을 필드의 이름으로 해준다.
            val updateMap = mapOf(
                "productName" to productVO.productName,
                "productCategory" to productVO.productCategory,
                "productPrice" to productVO.productPrice,
                "productDiscountRate" to productVO.productDiscountRate,
                "productStock" to productVO.productStock,
                "productDescription" to productVO.productDescription,
                "productSubImage" to productVO.productSubImage
            )
            // 수정한다.
            documentReference.update(updateMap).await()
        }

        // 서버에서 이미지 파일을 삭제한다.
        suspend fun removeImageFile(imageFileName:String){
            val imageReference = FirebaseStorage.getInstance().reference.child("image/$imageFileName")
            imageReference.delete().await()
        }

        suspend fun removeImageFiles(imageFileNames: List<String>) {
            val storageReference = FirebaseStorage.getInstance().reference

            imageFileNames.forEach { imageFileName ->
                val imageReference = storageReference.child("image/$imageFileName")
                imageReference.delete().await()
            }
        }

        // 서버에서 글을 삭제한다.
        suspend fun deleteProductData(productDocumentId:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("ProductData")
            val documentReference = collectionReference.document(productDocumentId)
            documentReference.delete().await()
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectProductDataOneById(documentId:String) : ProductVO{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("ProductData")
            val documentReference = collectionReference.document(documentId)
            val documentSnapShot = documentReference.get().await()
            val productVO = documentSnapShot.toObject(ProductVO::class.java)!!
            return productVO
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectOrderDataOneById(documentId:String) : OrderVO{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderData")
            val documentReference = collectionReference.document(documentId)
            val documentSnapShot = documentReference.get().await()
            val orderVO = documentSnapShot.toObject(OrderVO::class.java)!!
            return orderVO
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectCustomerDataOneById(documentId:String) : CustomerVO {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(documentId)
            val documentSnapShot = documentReference.get().await()
            val customerVO = documentSnapShot.toObject(CustomerVO::class.java)!!
            return customerVO
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectPickupLocDataOneById(documentId:String) : PickupLocationVO {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("PickupLocationData")
            val documentReference = collectionReference.document(documentId)
            val documentSnapShot = documentReference.get().await()
            val pickupLocVO = documentSnapShot.toObject(PickupLocationVO::class.java)!!
            return pickupLocVO
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectOrderPackageDataOneById(documentId:String) : OrderPackageVO {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderPackageData")
            val documentReference = collectionReference.document(documentId)
            val documentSnapShot = documentReference.get().await()
            val orderPackageVO = documentSnapShot.toObject(OrderPackageVO::class.java)!!
            return orderPackageVO
        }

        // 이미지 데이터를 가져온다.
        suspend fun gettingImage(imageFileName: String, isMain: Boolean): Uri {
            val storageReference = FirebaseStorage.getInstance().reference
            val folder = if (isMain) "main" else "sub" // 메인 이미지는 "main/", 서브 이미지는 "sub/"
            val childStorageReference = storageReference.child("image/$imageFileName")

            return try {
                childStorageReference.downloadUrl.await()
            } catch (e: Exception) {
                Log.e("StorageError", "Error fetching image: ${e.message}")
                Uri.EMPTY // 예외 발생 시 빈 URI 반환
            }
        }
    }
}