package com.lion.judamie_seller.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.adapter.ImageSettingAdapter
import com.lion.judamie_seller.databinding.FragmentAddProductBinding
import com.lion.judamie_seller.model.ImageData
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.AddProductViewModel

class AddProductFragment() : Fragment() {

    private lateinit var fragmentAddProductBinding: FragmentAddProductBinding
    private lateinit var sellerActivity: SellerActivity
    // ViewModel 초기화
    private val addProductViewModel: AddProductViewModel by lazy {
        AddProductViewModel(this@AddProductFragment)
    }

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var isMainImage = false

    private lateinit var mainImagesAdapter: ImageSettingAdapter
    private lateinit var subImagesAdapter: ImageSettingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 데이터 바인딩 설정
        fragmentAddProductBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product, container, false)
        fragmentAddProductBinding.addProductViewModel = AddProductViewModel(this@AddProductFragment)
        fragmentAddProductBinding.lifecycleOwner = this@AddProductFragment

        sellerActivity = activity as SellerActivity

        // RecyclerView 초기화
        setupRecyclerViews()

        setupImagePicker()

        return fragmentAddProductBinding.root
    }

    private fun setupRecyclerViews() {
        // 메인 이미지 RecyclerView 설정
        mainImagesAdapter = ImageSettingAdapter { position ->
            mainImagesAdapter.removeImage(position)
        }

        fragmentAddProductBinding.recyclerViewMainImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mainImagesAdapter
        }

        // 초기 대표 이미지 추가
        mainImagesAdapter.addImage(ImageData(imageUrl = "res/drawable/ic_image_placeholder.xml", isMainImage = true, isDefault = true))

        // 서브 이미지 RecyclerView 설정
        subImagesAdapter = ImageSettingAdapter { position ->
            subImagesAdapter.removeImage(position)
        }

        fragmentAddProductBinding.recyclerViewSubImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = subImagesAdapter
        }

        // 초기 추가 이미지 추가
        subImagesAdapter.addImage(ImageData(imageUrl = "res/drawable/ic_image_placeholder.xml", isMainImage = false, isDefault = true))

        // 카테고리 리스트 설정
        val categories = listOf("소주", "맥주", "위스키", "막걸리")
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        fragmentAddProductBinding.autoCompleteCategory.setAdapter(arrayAdapter)

        // 드롭다운 선택 이벤트
        fragmentAddProductBinding.autoCompleteCategory.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = categories[position]
            addProductViewModel.productCategory.value = selectedCategory // Null 문제 해결
        }

        // 대표 이미지 추가 버튼
        fragmentAddProductBinding.buttonAddMainImage.setOnClickListener {
            isMainImage = true
            openGallery()
        }

        // 추가 이미지 추가 버튼
        fragmentAddProductBinding.buttonAddAdditionalImage.setOnClickListener {
            isMainImage = false
            openGallery()
        }
    }

    private fun setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                if (imageUri != null) {
                    if (isMainImage) {
                        // 대표 이미지를 업데이트
                        mainImagesAdapter.setImages(listOf(ImageData(imageUrl = imageUri.toString(), isMainImage = true, isDefault = false)))
                    } else {
                        if (subImagesAdapter.Items.any { it.isDefault })  {
                            subImagesAdapter.setImages(
                                listOf(
                                    ImageData(
                                        imageUrl = imageUri.toString(),
                                        isMainImage = true,
                                        isDefault = false
                                    )
                                )
                            )
                        } else {
                            subImagesAdapter.addImage(
                                ImageData(
                                    imageUrl = imageUri.toString(),
                                    isMainImage = false,
                                    isDefault = false
                                )
                            )
                            // 디버깅 로그 추가
                            println("ADDIMAGE: Sub image added, total items: ${subImagesAdapter.Items.size}")
                        }
                    }
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    fun moveToProductManagementFragment() {
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_ADD_PRODUCT)
    }
}
