package com.lion.judamie_seller.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.R
import com.lion.judamie_seller.fragment.AddProductFragment

class AddProductViewModel(val addProductFragment: AddProductFragment) : ViewModel() {
    val textFieldProductNameEditTextText = MutableLiveData("")
    val productCategory = MutableLiveData<String>()
    val textFieldProductPriceEditTextText = MutableLiveData("")
    val textFieldProductDiscountRateEditTextText = MutableLiveData("")
    val textFieldProductStockEditTextText = MutableLiveData("")
    val textFieldProductDetailEditTextText = MutableLiveData("")

    companion object {
        @JvmStatic
        @BindingAdapter("onNavigationClickAddProduct")
        fun onNavigationClickAddProduct(
            materialToolbar: MaterialToolbar,
            addProductFragment: AddProductFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                addProductFragment.moveToProductManagementFragment()
            }
        }


        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_placeholder) // 기본 이미지
                    .into(view)
            } else {
                view.setImageResource(R.drawable.ic_image_placeholder) // 기본 이미지
            }
        }
    }
}