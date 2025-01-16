package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentProductManagementBinding
import com.lion.judamie_seller.viewmodel.ProductManagementViewModel


class ProductManagementFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentProductManagementViewBinding: FragmentProductManagementBinding
    lateinit var sellerActivity: SellerActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentProductManagementViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_management, container, false)
        fragmentProductManagementViewBinding.productManagementViewModel = ProductManagementViewModel(this@ProductManagementFragment)
        fragmentProductManagementViewBinding.lifecycleOwner = this@ProductManagementFragment

        sellerActivity = activity as SellerActivity

        return fragmentProductManagementViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        mainFragment.removeFragment(MainFragment.SubFragmentName.PRODUCT_MANAGEMENT_FRAGMENT)
    }
}