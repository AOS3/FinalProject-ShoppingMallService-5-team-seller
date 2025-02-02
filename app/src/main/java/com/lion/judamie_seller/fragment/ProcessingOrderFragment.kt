package com.lion.judamie_seller.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentProcessingOrderBinding
import com.lion.judamie_seller.model.CustomerModel
import com.lion.judamie_seller.model.OrderModel
import com.lion.judamie_seller.model.PickupLocationModel
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.ProcessingOrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ProcessingOrderFragment : Fragment() {
    lateinit var fragmentProcessingOrderBinding: FragmentProcessingOrderBinding
    lateinit var sellerActivity: SellerActivity

    private lateinit var orderDocumentId: String
    private lateinit var customerDocumentId: String
    private lateinit var productDocumentId: String
    private lateinit var pickupLocDocumentId: String

    // 글 데이터를 담을 변수
    lateinit var orderModel: OrderModel
    lateinit var customerModel: CustomerModel
    lateinit var productModel: ProductModel
    lateinit var pickupLocModel: PickupLocationModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentProcessingOrderBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_processing_order, container, false)
        fragmentProcessingOrderBinding.processingOrderViewModel = ProcessingOrderViewModel(this@ProcessingOrderFragment)
        fragmentProcessingOrderBinding.lifecycleOwner = this@ProcessingOrderFragment

        sellerActivity = activity as SellerActivity

        gettingArguments()

        settingProductData()

        return fragmentProcessingOrderBinding.root
    }

    // arguments의 값을 변수에 담아준다.
    fun gettingArguments() {
        val args = arguments
        orderDocumentId = args?.getString("orderDocumentId")!!
        customerDocumentId = args?.getString("customerDocumentId")!!
        productDocumentId = args?.getString("productDocumentId")!!
        pickupLocDocumentId = args?.getString("pickupLocDocumentId")!!
    }

    // 글 데이터를 가져와 보여주는 메서드
    @SuppressLint("NewApi")
    fun settingProductData() {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                SellerService.selectOrderDataOneById(orderDocumentId)
            }
            orderModel = work1.await()

            val work2 = async(Dispatchers.IO) {
                SellerService.selectCustomerDataOneById(customerDocumentId)
            }
            customerModel = work2.await()

            val work3 = async(Dispatchers.IO) {
                SellerService.selectProductDataOneById(productDocumentId)
            }
            productModel = work3.await()

            val work4 = async(Dispatchers.IO) {
                SellerService.selectPickupLocDataOneById(pickupLocDocumentId)
            }
            pickupLocModel = work4.await()

            fragmentProcessingOrderBinding.apply {
                // userDocumentID로 유저 닉네임 갖고와야됨
                processingOrderViewModel?.textViewCustomerText?.value = customerModel.userId
                val instant = Instant.ofEpochMilli(orderModel.orderTime)
                val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formattedDate = localDateTime.format(formatter)
                processingOrderViewModel?.textViewOrderDateText?.value = formattedDate
                // productDocumentID로 이름 갖고와야됨
                processingOrderViewModel?.textViewProductText?.value = productModel.productName
                val price = (productModel.productPrice * (100 - productModel.productDiscountRate) * 0.01).toInt()
                processingOrderViewModel?.textViewPriceText?.value = price.toString()
                processingOrderViewModel?.textViewQuantityText?.value = orderModel.orderCount.toString()
                // PickupLocationData에서 pickupLocDocumentID로 갖고와야됨
                val address = pickupLocModel.pickupLocStreetAddress + " " + pickupLocModel.pickupLocAddressDetail
                processingOrderViewModel?.textViewPickupLocation?.value = address
                // processingOrderViewModel?.textViewPickupText?.value = orderModel.pickupLocDocumentId
            }

//            // 첨부 이미지가 있다면
//            if (productModel.productMainImage != "none") {
//                val work1 = async(Dispatchers.IO) {
//                    // 이미지에 접근할 수 있는 uri를 가져온다.
//                    SellerService.gettingMainImage(productModel.productMainImage)
//                }
//
//                val imageUri = work1.await()
//                sellerActivity.showServiceMainImage(
//                    imageUri,
//                    fragmentShowOneProductDetailBinding.imageViewMainImage
//                )
//                fragmentShowOneProductDetailBinding.imageViewMainImage.isVisible = true
//            }
//            Log.d("SubImageLog", "${productModel.productSubImage.isNotEmpty()}")
//
//            // SubImages 표시
//            if (productModel.productSubImage.isNotEmpty()) {
//                // SubImage가 존재하면 이미지 URIs를 가져온다.
//                val subImageUris = SellerService.gettingSubImages(productModel.productSubImage)
//                Log.d("SubImageLog", "${productModel.productSubImage}")
//                // URI 리스트를 어댑터에 전달
//                fragmentShowOneProductDetailBinding.recyclerViewSubImages.layoutManager = LinearLayoutManager(context)
//                fragmentShowOneProductDetailBinding.recyclerViewSubImages.adapter = ImageAdapter(subImageUris)
//                fragmentShowOneProductDetailBinding.recyclerViewSubImages.isVisible = true
//            } else {
//                // SubImage가 없을 경우 기본 이미지를 표시
//                val defaultImageUri: Uri? = null  // 기본 이미지 표시를 위한 URI (null이면 Glide에서 기본 이미지로 대체)
//                val defaultUris = listOf(defaultImageUri)
//
//                // 기본 이미지 URI를 어댑터에 전달
//                fragmentShowOneProductDetailBinding.recyclerViewSubImages.layoutManager = LinearLayoutManager(context)
//                fragmentShowOneProductDetailBinding.recyclerViewSubImages.adapter = ImageAdapter(defaultUris)
//                fragmentShowOneProductDetailBinding.recyclerViewSubImages.isVisible = true
//            }
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_ORDER)
    }

    fun buttonDeposit(){
        // TODO 판매자에게 입금처리하기(후에 구현)
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_ORDER)
    }
}