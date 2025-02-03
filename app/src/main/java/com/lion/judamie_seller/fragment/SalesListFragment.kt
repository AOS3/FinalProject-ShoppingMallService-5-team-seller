package com.lion.judamie_seller.fragment

import android.os.Bundle
import android.util.Log
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
import com.lion.judamie_seller.model.OrderModel
import com.lion.judamie_seller.model.OrderPackageModel
import com.lion.judamie_seller.repository.SellerRepository
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.SalesListViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.RowOrderCategoryListViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.RowSalesListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SalesListFragment() : Fragment() {

    lateinit var fragmentSalesListBinding: FragmentSalesListBinding
    lateinit var sellerActivity: SellerActivity

    var orderDataList = mutableListOf<OrderPackageModel>()

    var sellerStoreName: String? = null
    var recyclerViewList = mutableListOf<OrderPackageModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSalesListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sales_list, container, false)
        fragmentSalesListBinding.salesListViewModel = SalesListViewModel(this@SalesListFragment)
        fragmentSalesListBinding.lifecycleOwner = this@SalesListFragment

        sellerActivity = activity as SellerActivity

        sellerStoreName = arguments?.getString("sellerStoreName")

        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()

        refreshCategoryRecyclerView()

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

    fun refreshCategoryRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                // 선택된 카테고리(`productType`)에 해당하는 데이터만 가져옴
                SellerService.gettingOrderPackageList()
            }

            recyclerViewList = work1.await()

            // RecyclerView 업데이트
            fragmentSalesListBinding.recyclerviewSalesList.adapter?.notifyDataSetChanged()
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
                val dataBundle = Bundle()
                dataBundle.putString("customerDocumentId", recyclerViewList[mainViewHolder.adapterPosition].orderOwnerId)
                dataBundle.putString("orderPackageDocumentId", recyclerViewList[mainViewHolder.adapterPosition].orderPackageDocumentId)
                dataBundle.putString("sellerStoreName",sellerStoreName)
                sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_DETAIL_SALES, true, true, dataBundle)
            }
            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowSalesListBinding.rowSalesListViewModel?.apply {
                // 여기서 productName을 텍스트 뷰에 바인딩
                textViewSalesList?.value = "거래${position+1}"
            }
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_SALES_LIST)
    }
}