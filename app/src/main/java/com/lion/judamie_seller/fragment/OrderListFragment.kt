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
import com.lion.judamie_seller.databinding.FragmentOrderListBinding
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.OrderListViewModel
import com.lion.judamie_seller.viewmodel.rowviewmodel.productViewModel

class OrderListFragment() : Fragment() {

    lateinit var fragmentOrderListBinding: FragmentOrderListBinding
    lateinit var sellerActivity: SellerActivity

    private val categories = ProductType.values()

    private val productViewModel: productViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentOrderListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list, container, false)
        fragmentOrderListBinding.orderListViewModel = OrderListViewModel(this@OrderListFragment)
        fragmentOrderListBinding.lifecycleOwner = this@OrderListFragment

        sellerActivity = activity as SellerActivity

        fragmentOrderListBinding.apply {
            // ViewPager2의 어댑터를 설정한다.
            pager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

            // TabLayout과 ViewPager2가 상호 작용을 할 수 있도록 연동시켜준다.
            val tabLayoutMediator = TabLayoutMediator(tabLayout, pager) { tab, position ->
                tab.text = categories[position].str
            }
            tabLayoutMediator.attach()
        }

        return fragmentOrderListBinding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_ORDER_LIST)
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
                putString("sellerDocumentId", productViewModel.sellerDocumentId)
            }

            return OrderCategoryFragment().apply {
                arguments = dataBundle
            }
        }
    }
}