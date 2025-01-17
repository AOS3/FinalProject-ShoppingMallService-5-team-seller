package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentOrderListBinding
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.OrderListViewModel

class OrderListFragment() : Fragment() {

    lateinit var fragmentOrderListBinding: FragmentOrderListBinding
    lateinit var sellerActivity: SellerActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentOrderListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list, container, false)
        fragmentOrderListBinding.orderListViewModel = OrderListViewModel(this@OrderListFragment)
        fragmentOrderListBinding.lifecycleOwner = this@OrderListFragment

        sellerActivity = activity as SellerActivity

        return fragmentOrderListBinding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_ORDER_LIST)
    }
}