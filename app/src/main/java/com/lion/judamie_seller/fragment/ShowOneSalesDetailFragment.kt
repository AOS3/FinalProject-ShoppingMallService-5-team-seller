package com.lion.judamie_seller.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentSalesListBinding
import com.lion.judamie_seller.databinding.FragmentShowOneSalesDetailBinding
import com.lion.judamie_seller.model.CustomerModel
import com.lion.judamie_seller.model.OrderModel
import com.lion.judamie_seller.model.OrderPackageModel
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.repository.SellerRepository
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.SalesListViewModel
import com.lion.judamie_seller.viewmodel.ShowOneSalesDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ShowOneSalesDetailFragment : Fragment() {


    lateinit var fragmentShowOneSalesDetailBinding: FragmentShowOneSalesDetailBinding
    lateinit var sellerActivity: SellerActivity

    private lateinit var orderOwnerId: String
    private lateinit var orderPackageDocumentId: String
    private lateinit var sellerStoreName: String

    lateinit var customerModel: CustomerModel
    lateinit var orderPackageModel: OrderPackageModel
    var orderList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentShowOneSalesDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_one_sales_detail,
            container,
            false
        )
        fragmentShowOneSalesDetailBinding.showOneSalesDetailViewModel =
            ShowOneSalesDetailViewModel(this@ShowOneSalesDetailFragment)
        fragmentShowOneSalesDetailBinding.lifecycleOwner = this@ShowOneSalesDetailFragment

        sellerActivity = activity as SellerActivity

        gettingArguments()

        settingProductData()

        return fragmentShowOneSalesDetailBinding.root
    }

    fun gettingArguments() {
        val args = arguments
        orderOwnerId = args?.getString("customerDocumentId")!!
        orderPackageDocumentId = args?.getString("orderPackageDocumentId")!!
        sellerStoreName = args?.getString("sellerStoreName")!!
    }

    // 글 데이터를 가져와 보여주는 메서드
    @SuppressLint("NewApi")
    fun settingProductData() {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                SellerService.selectCustomerDataOneById(orderOwnerId)
            }
            customerModel = work1.await()

            val work2 = async(Dispatchers.IO) {
                SellerService.selectOrderPackageDataOneById(orderPackageDocumentId)
            }
            orderPackageModel = work2.await()
            orderList.addAll(orderPackageModel.orderDataList)
            val timeList = mutableListOf<Long>() // orderDocumentId와 orderTransactionTime을 저장할 리스트
            val productDataList = mutableListOf<String>()
            val orderDataList = mutableListOf<String>()

            orderList.forEachIndexed { index, order ->
                val orderList = SellerService.gettingOrderList(order)

                orderList.forEach { orderModel ->
                    timeList.add(orderModel.orderTransactionTime)
                    productDataList.add(orderModel.productDocumentId)
                    orderDataList.add(orderModel.orderDocumentId)
                }
            }

            val filteredList = mutableListOf<OrderModel>()

            orderDataList.forEachIndexed { index, orderDocumentId ->
                val orderData = SellerService.gettingOrderByDocumentId(orderDocumentId)

                orderData?.forEach { orderModel ->
                    if (orderModel.sellerDocumentId == sellerStoreName) {
                        filteredList.add(orderModel)
                    }
                }
            }

            val filteredProductList = mutableListOf<ProductModel>()
            productDataList.forEachIndexed { index, productDocumentId ->
                val productData = SellerService.gettingProductByDocumentId(productDocumentId)

                productData?.forEach { productModel ->
                    if (productModel.productSeller == sellerStoreName) {
                        filteredProductList.add(productModel)
                    }
                }
            }

            val productName = mutableListOf<String>()
            val productPrice = mutableListOf<Int>()
            filteredProductList.forEach { productModel ->
                productName.add(productModel.productName)
                productPrice.add(productModel.productPrice)
            }

            val productCount = mutableListOf<Int>()
            val orderTime = mutableListOf<Long>()
            filteredList.forEach { orderModel ->
                orderTime.add(orderModel.orderTime)
                productCount.add(orderModel.orderCount)
            }

            val latestTime = if (timeList.isNotEmpty()) {
                timeList.max()  // Long 타입에서 최대값을 찾음
            } else {
                null
            }

            val latestOrderTime = if (orderTime.isNotEmpty()) {
                orderTime.max()  // Long 타입에서 최대값을 찾음
            } else {
                null
            }
            fragmentShowOneSalesDetailBinding.apply {
                showOneSalesDetailViewModel?.textViewCustomerText?.value = customerModel.userId
                latestOrderTime?.let { timeInMillis ->
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val formattedDate = dateFormat.format(Date(timeInMillis))
                    showOneSalesDetailViewModel?.textViewOrderDateText?.value = formattedDate
                }
                latestTime?.let { timeInMillis ->
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val formattedDate = dateFormat.format(Date(timeInMillis))
                    showOneSalesDetailViewModel?.textViewTransactionDateText?.value = formattedDate
                }
                val productNameString = productName.joinToString(", ")
                showOneSalesDetailViewModel?.textViewProductText?.value = productNameString
                val orderCountString = productCount.joinToString(", ")
                showOneSalesDetailViewModel?.textViewQuantityText?.value = orderCountString
                val productPriceString = productPrice.joinToString(", ")
                showOneSalesDetailViewModel?.textViewPriceText?.value = productPriceString
                val index = productName.size
                var totalPrice = 0
                var totalPriceSum = 0
                for (i in 0 until index) {
                    totalPrice = productPrice[i] * productCount[i]
                    totalPriceSum += totalPrice
                }
                showOneSalesDetailViewModel?.textViewTotalPriceText?.value = totalPriceSum.toString()
            }
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment() {
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_SALES)
    }
}

