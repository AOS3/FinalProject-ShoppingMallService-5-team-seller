package com.lion.judamie_seller.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentCategoryBinding
import com.lion.judamie_seller.databinding.RowProductCategoryListBinding
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.CategoryViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.RowProductCategoryListViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.productViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProductCategoryFragment : Fragment() {
    lateinit var fragmentCategoryBinding: FragmentCategoryBinding
    lateinit var sellerActivity: SellerActivity

    var categoryName: String? = null
    lateinit var productType: ProductType

    private val productViewModel: productViewModel by activityViewModels()

    var recyclerViewList = mutableListOf<ProductModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        fragmentCategoryBinding.categoryViewModel = CategoryViewModel(this@ProductCategoryFragment)
        fragmentCategoryBinding.lifecycleOwner = this@ProductCategoryFragment

        sellerActivity = activity as SellerActivity

        recyclerViewList.clear()

        val productTypeNumber = arguments?.getInt("ProductType") ?: ProductType.PRODUCT_TYPE_ALL.number
        productType = ProductType.values().first { it.number == productTypeNumber }

        categoryName = arguments?.getString("categoryName") ?: "전체"

        settingRecyclerView()

        refreshCategoryRecyclerView()

        return fragmentCategoryBinding.root
    }

    // RecyclerView 설정 메서드
    fun settingRecyclerView() {
        fragmentCategoryBinding.apply {
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
                SellerService.gettingProductList(productType)
            }
            recyclerViewList = work1.await()

            // RecyclerView 업데이트
            fragmentCategoryBinding.recyclerviewCategoryList.adapter?.notifyDataSetChanged()
        }
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder>() {

        inner class MainViewHolder(val rowCategoryListBinding: RowProductCategoryListBinding) :
            RecyclerView.ViewHolder(rowCategoryListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowCategoryListBinding = DataBindingUtil.inflate<RowProductCategoryListBinding>(
                layoutInflater,
                R.layout.row_product_category_list,
                parent,
                false
            )

            // ViewModel 바인딩
            rowCategoryListBinding.rowCategoryListViewModel = RowProductCategoryListViewModel(this@ProductCategoryFragment)
            rowCategoryListBinding.lifecycleOwner = this@ProductCategoryFragment

            val mainViewHolder = MainViewHolder(rowCategoryListBinding)

            // 리사이클러뷰 항목 클릭 시 상세 화면으로 이동
            rowCategoryListBinding.root.setOnClickListener {
                val dataBundle = Bundle()
                dataBundle.putString("productDocumentId", recyclerViewList[mainViewHolder.adapterPosition].productDocumentId)
                sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_DETAIL_PRODUCT, true, true, dataBundle)
            }

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.size // 리사이클러뷰에 표시할 데이터 개수
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val product = recyclerViewList[position]
            holder.rowCategoryListBinding.rowCategoryListViewModel?.apply {
                // 여기서 productName을 텍스트 뷰에 바인딩
                textViewProductCategoryList?.value = product.productName // productName을 표시
            }
        }
    }
}