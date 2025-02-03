package com.lion.judamie_seller.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
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
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.SalesListViewModel
import com.lion.judamie_seller.viewmodel.ShowOneSalesDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ShowOneSalesDetailFragment : Fragment() {


    lateinit var fragmentShowOneSalesDetailBinding: FragmentShowOneSalesDetailBinding
    lateinit var sellerActivity: SellerActivity

    private lateinit var orderOwnerId: String
    private lateinit var orderPackageDocumentId: String
    private lateinit var orderDocumentIds: List<String>

    lateinit var customerModel: CustomerModel
    lateinit var orderPackageModel: OrderPackageModel
    lateinit var orderModels: List<OrderModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentShowOneSalesDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_one_sales_detail, container, false)
        fragmentShowOneSalesDetailBinding.showOneSalesDetailViewModel = ShowOneSalesDetailViewModel(this@ShowOneSalesDetailFragment)
        fragmentShowOneSalesDetailBinding.lifecycleOwner = this@ShowOneSalesDetailFragment

        sellerActivity = activity as SellerActivity

        gettingArguments()

        settingProductData()

        return fragmentShowOneSalesDetailBinding.root
    }

    fun gettingArguments() {
        val args = arguments
        orderOwnerId = args?.getString("customerDocumentId")!!
        orderPackageDocumentId= args?.getString("orderPackageDocumentId")!!
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

//            val work3 = async(Dispatchers.IO) {
//                // 여러 orderDocumentId 처리
//                orderPackageModel.orderDataList.forEach { orderData ->
//                    val orderDataResult = SellerService.selectOrderDataOneById(orderData)
//                    orderModels.add(orderDataResult) // 결과를 리스트에 추가
//                }
//                return@async orderModels  // 최종적으로 여러 OrderModel이 담긴 리스트 반환
//            }

            // 여러 OrderModel 처리
//            orderModels = work3.await()

            fragmentShowOneSalesDetailBinding.apply {
                showOneSalesDetailViewModel?.textViewCustomerText?.value = customerModel.userId
//                val instant = Instant.ofEpochMilli(orderModels.)
//                val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
//                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//                val formattedDate = localDateTime.format(formatter)
//                showOneSalesDetailViewModel?.textViewOrderDateText?.value = formattedDate
//                // showOneSalesDetailViewModel?.textViewTransactionDateText?.value =
//                showOneSalesDetailViewModel?.textViewProductText?.value
//                showOneSalesDetailViewModel?.textViewQuantityText?.value
//                showOneSalesDetailViewModel?.textViewPriceText?.value
//                showOneSalesDetailViewModel?.textViewTotalPriceText?.value
            }
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_SALES)
    }
}