package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentModifyInfoBinding
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.ModifyInfoViewModel

class ModifyInfoFragment() : Fragment() {

    lateinit var fragmentModifyInfoBinding: FragmentModifyInfoBinding
    lateinit var sellerActivity: SellerActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentModifyInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_info, container, false)
        fragmentModifyInfoBinding.modifyInfoViewModel = ModifyInfoViewModel(this@ModifyInfoFragment)
        fragmentModifyInfoBinding.lifecycleOwner = this@ModifyInfoFragment

        sellerActivity = activity as SellerActivity


        return fragmentModifyInfoBinding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_INFO)
    }
}