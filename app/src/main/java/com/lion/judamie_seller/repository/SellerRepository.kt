package com.lion.judamie_seller.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.vo.OrderVO
import com.lion.judamie_seller.vo.ProductVO
import kotlinx.coroutines.tasks.await
import java.io.File

class SellerRepository {
    companion object {
        // 이미지 데이터를 서버로 업로드 하는 메서드
        suspend fun uploadImage(sourceFilePath: String, serverFilePath: String) {
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
            val collectionReference = fireStore.collection("productData")
            val documentReference = collectionReference.add(productVO).await()
            return documentReference.id
        }

        // 글 목록을 가져오는 메서드
        suspend fun gettingProductList(productType: ProductType) : MutableList<Map<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("productData")
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

        // 글정보를 수정하는 메서드
        suspend fun updateProductData(productVO: ProductVO, productDocumentId:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("productData")
            val documentReference = collectionReference.document(productDocumentId)

            // 수정할 데이터를 맵에 담는다
            // 데이터의 이름을 필드의 이름으로 해준다.
            val updateMap = mapOf(
                "productTitle" to productVO.productName,
                "productCategory" to productVO.productCategory,
                "productPrice" to productVO.productPrice,
                "productDiscountRate" to productVO.productDiscountRate,
                "productStock" to productVO.productStock,
                "productDescription" to productVO.productDescription
            )
            // 수정한다.
            documentReference.update(updateMap).await()
        }

        // 서버에서 이미지 파일을 삭제한다.
        suspend fun removeImageFile(imageFileName:String){
            val imageReference = FirebaseStorage.getInstance().reference.child("image/$imageFileName.jpg")
            imageReference.delete().await()
        }

        // 서버에서 글을 삭제한다.
        suspend fun deleteProductData(productDocumentId:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("productData")
            val documentReference = collectionReference.document(productDocumentId)
            documentReference.delete().await()
        }

        // 글의 문서 id를 통해 글 데이터를 가져온다.
        suspend fun selectProductDataOneById(documentId:String) : ProductVO{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("productData")
            val documentReference = collectionReference.document(documentId)
            val documentSnapShot = documentReference.get().await()
            val productVO = documentSnapShot.toObject(ProductVO::class.java)!!
            return productVO
        }

        // 이미지 데이터를 가져온다.
        suspend fun gettingImage(imageFileName:String) : Uri{
            val storageReference = FirebaseStorage.getInstance().reference
            // 파일명을 지정하여 이미지 데이터를 가져온다.
            val childStorageReference = storageReference.child("image/$imageFileName")
            val imageUri = childStorageReference.downloadUrl.await()
            return imageUri
        }
    }
}