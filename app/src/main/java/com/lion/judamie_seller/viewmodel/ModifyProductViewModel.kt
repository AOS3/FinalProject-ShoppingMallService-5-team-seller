package com.lion.judamie_seller.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.lion.judamie_seller.R
import com.lion.judamie_seller.fragment.ModifyProductFragment

class ModifyProductViewModel(val ModifyProductFragment: ModifyProductFragment) : ViewModel() {
    val textFieldProductNameEditTextText = MutableLiveData("")
    val productCategory = MutableLiveData<String>()
    val textFieldProductPriceEditTextText = MutableLiveData("")
    val textFieldProductDiscountRateEditTextText = MutableLiveData("")
    val textFieldProductStockEditTextText = MutableLiveData("")
    val textFieldProductDescriptionEditTextText = MutableLiveData("")

    companion object {
        @JvmStatic
        @BindingAdapter("onNavigationClickModifyProduct")
        fun onNavigationClickModifyProduct(
            materialToolbar: MaterialToolbar,
            ModifyProductFragment: ModifyProductFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                ModifyProductFragment.moveToProductManagementFragment()
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