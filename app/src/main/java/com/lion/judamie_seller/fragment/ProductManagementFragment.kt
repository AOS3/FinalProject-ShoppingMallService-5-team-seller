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

    // 현재 글의 문서 id를 담을 변수
    lateinit var boardDocumentId:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentProductManagementViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_management, container, false)
        fragmentProductManagementViewBinding.productManagementViewModel = ProductManagementViewModel(this@ProductManagementFragment)
        fragmentProductManagementViewBinding.lifecycleOwner = this@ProductManagementFragment

        sellerActivity = activity as SellerActivity

        gettingArguments()

        settingToolbar()

        return fragmentProductManagementViewBinding.root
    }

    fun settingToolbar(){
        fragmentProductManagementViewBinding.apply {
            toolbar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuItemProductAdd -> {
                        // 글의 문서 번호를 전달한다.
                        val dataBundle = Bundle()
                        dataBundle.putString("sellerDocumentId", boardDocumentId)

                        // AddProductFragment로 이동
                        sellerActivity.replaceFragment(
                            SellerFragmentType.SELLER_TYPE_ADD_PRODUCT,
                            isAddToBackStack = true,
                            animate = true,
                            dataBundle = dataBundle
                        )
                    }
                }
                true
            }
        }
    }

    fun gettingArguments(){
        boardDocumentId = arguments?.getString("boardDocumentId")!!
    }

    fun moveToAddProduct() {
        sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_ADD_PRODUCT, true, true, null)
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_PRODUCT_MANAGEMENT)
    }
}