package com.lion.judamie_seller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentMainBinding
import com.lion.judamie_seller.viewmodel.MainViewModel

class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var sellerActivity: SellerActivity

    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        fragmentMainBinding.mainViewModel = MainViewModel(this@MainFragment)
        fragmentMainBinding.lifecycleOwner = this@MainFragment

        sellerActivity = activity as SellerActivity

        return fragmentMainBinding.root
    }

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: SubFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 프래그먼트 객체
        newFragment = when(fragmentName){
            // 게시글 목록 화면
            SubFragmentName.PRODUCT_MANAGEMENT_FRAGMENT -> ProductManagementFragment(this@MainFragment)

            SubFragmentName.ORDER_LIST_FRAGMENT -> OrderListFragment(this@MainFragment)

            SubFragmentName.SALES_LIST_FRAGMENT -> SalesListFragment(this@MainFragment)

            SubFragmentName.MODIFY_INFO_FRAGMENT -> ModifyInfoFragment(this@MainFragment)
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment?.arguments = dataBundle
        }

        // 프래그먼트 교체
        sellerActivity.supportFragmentManager.commit {

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

    // 회원 가입 화면으로 이동시키는 메서드
    fun moveToProductManagement(){
        replaceFragment(SubFragmentName.PRODUCT_MANAGEMENT_FRAGMENT, true, true, null)
    }

    // 회원 가입 화면으로 이동시키는 메서드
    fun moveToOrderList(){
        replaceFragment(SubFragmentName.ORDER_LIST_FRAGMENT, true, true, null)
    }

    // 회원 가입 화면으로 이동시키는 메서드
    fun moveToSalesList(){
        replaceFragment(SubFragmentName.SALES_LIST_FRAGMENT, true, true, null)
    }

    // 회원 가입 화면으로 이동시키는 메서드
    fun moveToModifyInfo(){
        replaceFragment(SubFragmentName.MODIFY_INFO_FRAGMENT, true, true, null)
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: SubFragmentName){
        sellerActivity.supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    // 하위 프래그먼트들의 이름
    enum class SubFragmentName(var number:Int, var str:String){
        // 게시글 목록 화면
        PRODUCT_MANAGEMENT_FRAGMENT(1, "ProductManagementFragment"),
        // 게시글 작성 화면
        ORDER_LIST_FRAGMENT(2, "OrderListFragment"),
        // 게시글 읽기  화면
        SALES_LIST_FRAGMENT(3, "SalesListFragment"),
        // 사용자 정보 수정 화면
        MODIFY_INFO_FRAGMENT(5, "ModifyInfoFragment"),
    }
}