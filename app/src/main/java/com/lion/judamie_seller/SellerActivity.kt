package com.lion.judamie_seller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.lion.judamie_seller.databinding.ActivitySellerBinding
import com.lion.judamie_seller.fragment.AddProductFragment
import com.lion.judamie_seller.fragment.MainFragment
import com.lion.judamie_seller.fragment.ModifyInfoFragment
import com.lion.judamie_seller.fragment.ModifyProductFragment
import com.lion.judamie_seller.fragment.OrderListFragment
import com.lion.judamie_seller.fragment.ProductManagementFragment
import com.lion.judamie_seller.fragment.SalesListFragment
import com.lion.judamie_seller.fragment.ShowOneProductDetailFragment
import com.lion.judamie_seller.fragment.ShowOneSalesDetailFragment
import com.lion.judamie_seller.util.SellerFragmentType


class SellerActivity : AppCompatActivity() {

    lateinit var activitySellerBinding: ActivitySellerBinding


    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seller)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 첫 프래그먼트를 보여준다.
        replaceFragment(SellerFragmentType.SELLER_TYPE_MAIN, false, false, null)
    }

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: SellerFragmentType, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 프래그먼트 객체
        newFragment = when(fragmentName){

            SellerFragmentType.SELLER_TYPE_MAIN -> MainFragment()

            SellerFragmentType.SELLER_TYPE_PRODUCT_MANAGEMENT -> ProductManagementFragment()

            SellerFragmentType.SELLER_TYPE_ORDER_LIST -> OrderListFragment()

            SellerFragmentType.SELLER_TYPE_SALES_LIST -> SalesListFragment()

            SellerFragmentType.SELLER_TYPE_INFO -> ModifyInfoFragment()

            SellerFragmentType.SELLER_TYPE_ADD_PRODUCT -> AddProductFragment()

            SellerFragmentType.SELLER_TYPE_MODIFY_PRODUCT -> ModifyProductFragment()

            SellerFragmentType.SELLER_TYPE_SHOW_ONE_PRODUCT_DETAIL -> ShowOneProductDetailFragment()

            SellerFragmentType.SELLER_TYPE_DETAIL_PRODUCT -> ShowOneProductDetailFragment()

            SellerFragmentType.SELLER_TYPE_DETAIL_ORDER -> OrderListFragment()

            SellerFragmentType.SELLER_TYPE_DETAIL_SALES -> ShowOneSalesDetailFragment()
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment?.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {

            if(animate) {
                // 만약 이전 프래그먼트가 있다면
                if(oldFragment != null){
                    oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                    oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                }

                newFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.fragmentContainerViewBoard, newFragment!!)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: SellerFragmentType){
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}