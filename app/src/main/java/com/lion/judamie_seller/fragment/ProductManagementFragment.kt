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
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.ProductManagementViewModel


class ProductManagementFragment() : Fragment() {

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

        settingToolbar()

        return fragmentProductManagementViewBinding.root
    }

    fun settingToolbar(){
        fragmentProductManagementViewBinding.apply {
            toolbar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuItemProductAdd -> {

                        // AddProductFragment로 이동
                        sellerActivity.replaceFragment(
                            SellerFragmentType.SELLER_TYPE_ADD_PRODUCT,
                            isAddToBackStack = true,
                            animate = true,
                            dataBundle = null
                        )
                    }
                }
                true
            }
        }
    }

    fun moveToAddProduct() {
        sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_ADD_PRODUCT, true, true, null)
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_PRODUCT_MANAGEMENT)
    }
}