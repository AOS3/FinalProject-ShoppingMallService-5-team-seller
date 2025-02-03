package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.UserFragmentName
import com.lion.judamie_seller.databinding.FragmentMainBinding
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.MainViewModel

class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var sellerActivity: SellerActivity

    private lateinit var sellerStoreName: String
    private lateinit var sellerDocumentId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        fragmentMainBinding.mainViewModel = MainViewModel(this@MainFragment)
        fragmentMainBinding.lifecycleOwner = this@MainFragment

        sellerActivity = activity as SellerActivity

        val dataBundle = Bundle()
        dataBundle.putInt("SellerType", SellerFragmentType.SELLER_TYPE_MAIN.number)

        gettingArguments()

        return fragmentMainBinding.root
    }

    // arguments의 값을 변수에 담아준다.
    fun gettingArguments() {
        val args = arguments
        sellerStoreName = args?.getString("sellerStoreName")!!
        sellerDocumentId = args?.getString("sellerDocumentId")!!
    }

    // 상품 관리 화면으로 이동시키는 메서드
    fun moveToProductManagement() {
        // 데이터를 담는다.
        val dataBundle = Bundle()
        dataBundle.putString("sellerDocumentId", sellerDocumentId)
        dataBundle.putString("sellerStoreName", sellerStoreName)
        sellerActivity.replaceFragment(
            SellerFragmentType.SELLER_TYPE_PRODUCT_MANAGEMENT,
            true,
            true,
            dataBundle
        )
    }

    // 주문 내역 화면으로 이동시키는 메서드
    fun moveToOrderList(){
        // 데이터를 담는다.
        val dataBundle = Bundle()
        dataBundle.putString("sellerDocumentId", sellerDocumentId)
        dataBundle.putString("sellerStoreName", sellerStoreName)
        sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_ORDER_LIST, true, true, dataBundle)
    }

    // 거래 내역 화면으로 이동시키는 메서드
    fun moveToSalesList(){
        // 데이터를 담는다.
        val dataBundle = Bundle()
        dataBundle.putString("sellerDocumentId", sellerDocumentId)
        dataBundle.putString("sellerStoreName", sellerStoreName)
        sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_SALES_LIST, true, true, dataBundle)
    }

    // 개인 정보 변경 화면으로 이동시키는 메서드
    fun moveToModifyInfo(){
        // 데이터를 담는다.
        val dataBundle = Bundle()
        dataBundle.putString("sellerDocumentId", sellerDocumentId)
        dataBundle.putString("sellerStoreName", sellerStoreName)
        sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_INFO, true, true, dataBundle)
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: SellerFragmentType) {
        sellerActivity.supportFragmentManager.popBackStack(
            fragmentName.str,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}