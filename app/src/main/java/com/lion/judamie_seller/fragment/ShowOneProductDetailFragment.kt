package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentShowOneProductDetailBinding
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.ShowOneProductDetailViewModel

class ShowOneProductDetailFragment : Fragment() {

    lateinit var fragmentShowOneProductDetailBinding: FragmentShowOneProductDetailBinding
    lateinit var sellerActivity: SellerActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentShowOneProductDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_one_product_detail, container, false)
        fragmentShowOneProductDetailBinding.showOneProductDetailViewModel = ShowOneProductDetailViewModel(this@ShowOneProductDetailFragment)
        fragmentShowOneProductDetailBinding.lifecycleOwner = this@ShowOneProductDetailFragment

        sellerActivity = activity as SellerActivity


        return fragmentShowOneProductDetailBinding.root
    }


    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_SALES)
    }
}