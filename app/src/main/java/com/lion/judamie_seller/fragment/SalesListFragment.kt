package com.lion.judamie_seller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentSalesListBinding
import com.lion.judamie_seller.databinding.RowSalesListBinding
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.SalesListViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.RowSalesListViewModel

class SalesListFragment() : Fragment() {

    lateinit var fragmentSalesListBinding: FragmentSalesListBinding
    lateinit var sellerActivity: SellerActivity

    // ReyclerView 구성을 위한 임시 데이터
    val tempList = Array(100) {
        "거래 ${it + 1}"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSalesListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sales_list, container, false)
        fragmentSalesListBinding.salesListViewModel = SalesListViewModel(this@SalesListFragment)
        fragmentSalesListBinding.lifecycleOwner = this@SalesListFragment

        sellerActivity = activity as SellerActivity

        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()

        return fragmentSalesListBinding.root
    }

    // 리사이클러뷰 구성 메서드
    fun settingRecyclerView() {
        fragmentSalesListBinding.apply {
            // RecyclerView에 어댑터를 설정한다.
            recyclerviewSalesList.adapter = RecyclerViewAdapter()
            // 한줄에 하나씩 배치한다
            recyclerviewSalesList.layoutManager = LinearLayoutManager(sellerActivity)
            val deco = MaterialDividerItemDecoration(sellerActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerviewSalesList.addItemDecoration(deco)
        }
    }

    // 메인 RecyclerView의 어뎁터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder>(){
        inner class MainViewHolder(val rowSalesListBinding: RowSalesListBinding) : RecyclerView.ViewHolder(rowSalesListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowTransactionsListBinding = DataBindingUtil.inflate<RowSalesListBinding>(layoutInflater, R.layout.row_sales_list, parent, false)
            rowTransactionsListBinding.rowSalesListViewModel = RowSalesListViewModel(this@SalesListFragment)
            rowTransactionsListBinding.lifecycleOwner = this@SalesListFragment

            val mainViewHolder = MainViewHolder(rowTransactionsListBinding)

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowTransactionsListBinding.root.setOnClickListener {

                sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_DETAIL_SALES, true, true, null)
            }

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return tempList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowSalesListBinding.rowSalesListViewModel?.textViewSalesList?.value = tempList[position]
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_SALES_LIST)
    }
}