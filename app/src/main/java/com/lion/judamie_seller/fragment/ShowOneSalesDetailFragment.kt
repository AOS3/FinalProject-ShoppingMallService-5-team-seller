package com.lion.judamie_seller.fragment

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
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.SalesListViewModel
import com.lion.judamie_seller.viewmodel.ShowOneSalesDetailViewModel

class ShowOneSalesDetailFragment : Fragment() {


    lateinit var fragmentShowOneSalesDetailBinding: FragmentShowOneSalesDetailBinding
    lateinit var sellerActivity: SellerActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentShowOneSalesDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_one_sales_detail, container, false)
        fragmentShowOneSalesDetailBinding.showOneSalesDetailViewModel = ShowOneSalesDetailViewModel(this@ShowOneSalesDetailFragment)
        fragmentShowOneSalesDetailBinding.lifecycleOwner = this@ShowOneSalesDetailFragment

        sellerActivity = activity as SellerActivity


        return fragmentShowOneSalesDetailBinding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_SALES)
    }
}