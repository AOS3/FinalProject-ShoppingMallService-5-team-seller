package com.lion.judamie_seller.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.ProcessingOrderViewModel

class ProcessingOrderFragment : Fragment() {

    lateinit var sellerActivity: SellerActivity
    private val viewModel: ProcessingOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_processing_order, container, false)
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