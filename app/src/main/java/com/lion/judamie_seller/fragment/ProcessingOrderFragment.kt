package com.lion.judamie_seller.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentProcessingOrderBinding
import com.lion.judamie_seller.model.CustomerModel
import com.lion.judamie_seller.model.OrderModel
import com.lion.judamie_seller.model.PickupLocationModel
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.OrderState
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
        fragmentProcessingOrderBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_processing_order, container, false)
        fragmentProcessingOrderBinding.processingOrderViewModel =
            ProcessingOrderViewModel(this@ProcessingOrderFragment)
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
                val price =
                    (productModel.productPrice * (100 - productModel.productDiscountRate) * 0.01).toInt()
                processingOrderViewModel?.textViewPriceText?.value = price.toString()
                processingOrderViewModel?.textViewQuantityText?.value =
                    orderModel.orderCount.toString()
                // PickupLocationData에서 pickupLocDocumentID로 갖고와야됨
                val address =
                    pickupLocModel.pickupLocStreetAddress + " " + pickupLocModel.pickupLocAddressDetail
                processingOrderViewModel?.textViewPickupLocation?.value = address
                when (orderModel.orderState) {
                    1 -> {
                        processingOrderViewModel?.textViewDeliveryText?.value = "물건을 픽업지에 전달하세요."
                        processingOrderViewModel?.textViewPickupText?.value = "배송 전입니다."
                        processingOrderViewModel?.textViewDepositText?.value = "배송 전입니다."
                        setUIEnabled()
                    }

                    2 -> {
                        processingOrderViewModel?.textViewDeliveryText?.value =
                            "사용자가 물건을 픽업하지 않았습니다."
                        processingOrderViewModel?.textViewPickupText?.value = "배송완료"
                        processingOrderViewModel?.textViewDepositText?.value = "픽업 전입니다."
                        setUIEnabled()
                    }

                    3 -> {
                        processingOrderViewModel?.textViewDeliveryText?.value = "픽업완료"
                        processingOrderViewModel?.textViewPickupText?.value = "픽업완료"
                        processingOrderViewModel?.textViewDepositText?.value = "입금 전입니다."
                        setUIEnabled()
                    }

                    4 -> {
                        processingOrderViewModel?.textViewDeliveryText?.value = "픽업완료"
                        processingOrderViewModel?.textViewPickupText?.value = "픽업완료"
                        processingOrderViewModel?.textViewDepositText?.value = "입금완료"
                        setUIEnabled()
                    }
                }
            }
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment() {
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_ORDER)
    }

    fun buttonDeposit() {
        CoroutineScope(Dispatchers.Main).launch {
            orderModel.orderState = OrderState.ORDER_STATE_DELIVERY.num
            SellerService.updateOderStateData(orderModel)
            sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_ORDER)
        }
    }

    fun setUIEnabled() {
        fragmentProcessingOrderBinding.apply {
            if (orderModel.orderState != 1) {
                buttonDeposit.isEnabled = false
                buttonDeposit.isVisible = false
            }
        }
    }
}