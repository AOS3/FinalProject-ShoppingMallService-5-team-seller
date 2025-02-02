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
import com.lion.judamie_seller.databinding.FragmentOrderCategoryBinding
import com.lion.judamie_seller.databinding.RowOrderCategoryListBinding
import com.lion.judamie_seller.model.OrderModel
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.OrderCategoryViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.RowOrderCategoryListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class OrderCategoryFragment : Fragment() {

    lateinit var fragmentOrderCategoryBinding: FragmentOrderCategoryBinding
    lateinit var sellerActivity: SellerActivity

    lateinit var productType: ProductType

    var recyclerViewList = mutableListOf<OrderModel>()

    var sellerDocumentId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentOrderCategoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_category, container, false)
        fragmentOrderCategoryBinding.orderCategoryViewModel =
            OrderCategoryViewModel(this@OrderCategoryFragment)
        fragmentOrderCategoryBinding.lifecycleOwner = this@OrderCategoryFragment

        sellerActivity = activity as SellerActivity

        sellerDocumentId = arguments?.getString("sellerDocumentId")

        recyclerViewList.clear()

        val productTypeNumber = arguments?.getInt("ProductType") ?: ProductType.PRODUCT_TYPE_ALL.number
        productType = ProductType.values().first { it.number == productTypeNumber }

        settingRecyclerView()

        refreshCategoryRecyclerView()

        return fragmentOrderCategoryBinding.root
    }

    // RecyclerView 설정 메서드
    fun settingRecyclerView() {
        fragmentOrderCategoryBinding.apply {
            // RecyclerView에 어댑터를 설정한다.
            recyclerviewCategoryList.adapter = RecyclerViewAdapter()
            // 한줄에 하나씩 배치한다
            recyclerviewCategoryList.layoutManager = LinearLayoutManager(sellerActivity)
            val deco = MaterialDividerItemDecoration(
                sellerActivity,
                MaterialDividerItemDecoration.VERTICAL
            )
            recyclerviewCategoryList.addItemDecoration(deco)
        }
    }

    fun refreshCategoryRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                // 선택된 카테고리(`productType`)에 해당하는 데이터만 가져옴
                SellerService.gettingOrderList(productType)
            }
            recyclerViewList = work1.await()
                .filter { it.sellerDocumentId == sellerDocumentId }.toMutableList()

            // RecyclerView 업데이트
            fragmentOrderCategoryBinding.recyclerviewCategoryList.adapter?.notifyDataSetChanged()
        }
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder>() {

        inner class MainViewHolder(val rowOrderCategoryViewBinding: RowOrderCategoryListBinding) :
            RecyclerView.ViewHolder(rowOrderCategoryViewBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowCategoryListBinding = DataBindingUtil.inflate<RowOrderCategoryListBinding>(
                layoutInflater,
                R.layout.row_order_category_list,
                parent,
                false
            )

            // ViewModel 바인딩
            rowCategoryListBinding.rowOrderCategoryListViewModel = RowOrderCategoryListViewModel(this@OrderCategoryFragment)
            rowCategoryListBinding.lifecycleOwner = this@OrderCategoryFragment

            val mainViewHolder = MainViewHolder(rowCategoryListBinding)

            // 리사이클러뷰 항목 클릭 시 상세 화면으로 이동
            rowCategoryListBinding.root.setOnClickListener {
                val dataBundle = Bundle()
                dataBundle.putString("orderDocumentId", recyclerViewList[mainViewHolder.adapterPosition].orderDocumentId)
                dataBundle.putString("customerDocumentId", recyclerViewList[mainViewHolder.adapterPosition].userDocumentId)
                dataBundle.putString("productDocumentId", recyclerViewList[mainViewHolder.adapterPosition].productDocumentId)
                dataBundle.putString("pickupLocDocumentId", recyclerViewList[mainViewHolder.adapterPosition].pickupLocDocumentId)
                sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_DETAIL_ORDER, true, true, dataBundle)
            }

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.size // 리사이클러뷰에 표시할 데이터 개수
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowOrderCategoryViewBinding.rowOrderCategoryListViewModel?.apply {
                // 여기서 productName을 텍스트 뷰에 바인딩
                textViewOrderListCategoryList?.value = "주문${position+1}" // productName을 표시
            }
        }
    }

}