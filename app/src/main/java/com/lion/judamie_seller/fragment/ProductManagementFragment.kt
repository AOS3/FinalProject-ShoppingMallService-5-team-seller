package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentProductManagementBinding
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.ProductManagementViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.productViewModel

class ProductManagementFragment() : Fragment() {

    lateinit var fragmentProductManagementViewBinding: FragmentProductManagementBinding
    lateinit var sellerActivity: SellerActivity

    private val productViewModel: productViewModel by activityViewModels()

    private val categories = ProductType.values()

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

        fragmentProductManagementViewBinding.apply {
            // ViewPager2의 어댑터를 설정한다.
            pager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

            // TabLayout과 ViewPager2가 상호 작용을 할 수 있도록 연동시켜준다.
            val tabLayoutMediator = TabLayoutMediator(tabLayout, pager) { tab, position ->
                tab.text = categories[position].str
            }
            tabLayoutMediator.attach()
        }

        return fragmentProductManagementViewBinding.root
    }

    private fun gettingArguments() {
        val sellerStoreName = arguments?.getString("sellerStoreName")
        val sellerDocumentId = arguments?.getString("sellerDocumentId")

        if (sellerStoreName != null && sellerDocumentId != null ) {
            // ViewModel에 값 저장
            productViewModel.sellerStoreName = sellerStoreName
            productViewModel.sellerDocumentId = sellerDocumentId
        }
    }

    fun settingToolbar(){
        fragmentProductManagementViewBinding.apply {
            toolbar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuItemProductAdd -> {
                        val dataBundle = Bundle()
                        dataBundle.putString("sellerDocumentId", productViewModel.sellerDocumentId)
                        dataBundle.putString("sellerStoreName", productViewModel.sellerStoreName)
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

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_PRODUCT_MANAGEMENT)
    }

    // ViewPager2의 어댑터
    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
        // ViewPager2를 통해 보여줄 프래그먼트의 개수
        override fun getItemCount(): Int {
            return categories.size // 탭의 개수만큼
        }

        // position번째에서 사용할 Fragment 객체를 생성해 반환하는 메서드
        override fun createFragment(position: Int): Fragment {
            val dataBundle = Bundle().apply {
                putString("categoryName", categories[position].str)
                putInt("ProductType", categories[position].number)
                putString("sellerStoreName", productViewModel.sellerStoreName)
                putString("sellerDocumentId", productViewModel.sellerDocumentId)
            }

            return ProductCategoryFragment().apply {
                arguments = dataBundle
            }
        }
    }
}