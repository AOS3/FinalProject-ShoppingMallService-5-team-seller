package com.lion.judamie_seller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentSalesListBinding
import com.lion.judamie_seller.viewmodel.SalesListViewModel

class SalesListFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentSalesListBinding: FragmentSalesListBinding
    lateinit var sellerActivity: SellerActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSalesListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sales_list, container, false)
        fragmentSalesListBinding.salesListViewModel = SalesListViewModel(this@SalesListFragment)
        fragmentSalesListBinding.lifecycleOwner = this@SalesListFragment

        sellerActivity = activity as SellerActivity

        return fragmentSalesListBinding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        mainFragment.removeFragment(MainFragment.SubFragmentName.SALES_LIST_FRAGMENT)
    }
}