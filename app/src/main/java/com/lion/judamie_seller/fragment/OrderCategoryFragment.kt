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
import com.lion.judamie_seller.databinding.FragmentOrderCategoryBinding
import com.lion.judamie_seller.databinding.RowOrderCategoryListBinding
import com.lion.judamie_seller.databinding.RowProductCategoryListBinding
import com.lion.judamie_seller.model.OrderModel
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.OrderCategoryViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.RowOrderCategoryListViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.RowProductCategoryListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class OrderCategoryFragment : Fragment() {

    lateinit var fragmentOrderCategoryBinding: FragmentOrderCategoryBinding
    lateinit var sellerActivity: SellerActivity

    var categoryName: String? = null
    lateinit var productType: ProductType

    var recyclerViewList = mutableListOf<OrderModel>()

    companion object {
        private const val ARG_CATEGORY_NAME = "categoryName"

        fun newInstance(categoryName: String): OrderCategoryFragment {
            val fragment = OrderCategoryFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY_NAME, categoryName)
            fragment.arguments = args
            return fragment
        }
    }

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

        recyclerViewList.clear()

        categoryName = arguments?.getString(ARG_CATEGORY_NAME)

        settingProductType()

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

            // RecyclerView 업데이트
            fragmentOrderCategoryBinding.recyclerviewCategoryList.adapter?.notifyDataSetChanged()
        }
    }

    // 게시판 타입 값을 담는 메서드
    fun settingProductType(){
        when(productType.number){
            ProductType.PRODUCT_TYPE_ALL.number -> {
                productType = ProductType.PRODUCT_TYPE_ALL
            }
            ProductType.PRODUCT_TYPE_WINE.number -> {
                productType = ProductType.PRODUCT_TYPE_WINE
            }
            ProductType.PRODUCT_TYPE_WHISKEY.number -> {
                productType = ProductType.PRODUCT_TYPE_WHISKEY
            }
            ProductType.PRODUCT_TYPE_VODKA.number -> {
                productType = ProductType.PRODUCT_TYPE_VODKA
            }
            ProductType.PRODUCT_TYPE_TEQUILA.number -> {
                productType = ProductType.PRODUCT_TYPE_TEQUILA
            }
            ProductType.PRODUCT_TYPE_DOMESTIC.number -> {
                productType = ProductType.PRODUCT_TYPE_DOMESTIC
            }
            ProductType.PRODUCT_TYPE_SAKE.number -> {
                productType = ProductType.PRODUCT_TYPE_SAKE
            }
            ProductType.PRODUCT_TYPE_RUM.number -> {
                productType = ProductType.PRODUCT_TYPE_RUM
            }
            ProductType.PRODUCT_TYPE_LIQUEUR.number -> {
                productType = ProductType.PRODUCT_TYPE_LIQUEUR
            }
            ProductType.PRODUCT_TYPE_CHINESE.number -> {
                productType = ProductType.PRODUCT_TYPE_CHINESE
            }
            ProductType.PRODUCT_TYPE_BRANDY.number -> {
                productType = ProductType.PRODUCT_TYPE_BRANDY
            }
            ProductType.PRODUCT_TYPE_BEER.number -> {
                productType = ProductType.PRODUCT_TYPE_BEER
            }
            ProductType.PRODUCT_TYPE_NON_ALCOHOL.number -> {
                productType = ProductType.PRODUCT_TYPE_NON_ALCOHOL
            }
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
                dataBundle.putString("orderDocumentId", recyclerViewList[mainViewHolder.adapterPosition].orderDocumentID)
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
                textViewOrderListCategoryList?.value = "$position" // productName을 표시
            }
        }
    }

}