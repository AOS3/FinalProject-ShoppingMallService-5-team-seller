package com.lion.judamie_seller.fragment

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.FragmentShowOneProductDetailBinding
import com.lion.judamie_seller.databinding.ItemImageDetailSettingBinding
import com.lion.judamie_seller.model.ImageData
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.ShowOneProductDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShowOneProductDetailFragment : Fragment() {

    lateinit var fragmentShowOneProductDetailBinding: FragmentShowOneProductDetailBinding
    lateinit var sellerActivity: SellerActivity
    private lateinit var productDocumentId: String

    // 글 데이터를 담을 변수
    lateinit var productModel: ProductModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentShowOneProductDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_one_product_detail,
            container,
            false
        )
        fragmentShowOneProductDetailBinding.showOneProductDetailViewModel =
            ShowOneProductDetailViewModel(this@ShowOneProductDetailFragment)
        fragmentShowOneProductDetailBinding.lifecycleOwner = this@ShowOneProductDetailFragment

        sellerActivity = activity as SellerActivity

        gettingArguments()

        settingToolbar()

        // 데이터를 불러오는 메서드
        settingProductData()

        return fragmentShowOneProductDetailBinding.root
    }


    // arguments의 값을 변수에 담아준다.
    fun gettingArguments() {
        productDocumentId = arguments?.getString("productDocumentId")!!
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentShowOneProductDetailBinding.apply {
            toolbar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuItemProductModify -> {
                        // 글의 문서 번호를 전달한다.
                        val dataBundle = Bundle()
                        dataBundle.putString("productDocumentId", productDocumentId)
                        sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_MODIFY_PRODUCT, true, true, dataBundle)
                    }
                    R.id.menuItemProductDelete -> {
                        val builder = MaterialAlertDialogBuilder(sellerActivity)
                        builder.setTitle("상품 삭제")
                        builder.setMessage("삭제시 복구할 수 없습니다")
                        builder.setNegativeButton("취소", null)
                        builder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                            proProductDelete()
                        }
                        builder.show()
                    }
                }
                true
            }
        }
    }

    // 글 데이터를 가져와 보여주는 메서드
    fun settingProductData() {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                SellerService.selectProductDataOneById(productDocumentId)
            }
            productModel = work1.await()

            fragmentShowOneProductDetailBinding.apply {
                showOneProductDetailViewModel?.textProductNameText?.value = productModel.productName
                showOneProductDetailViewModel?.textProductCategoryText?.value =
                    productModel.productCategory
                showOneProductDetailViewModel?.textProductPriceText?.value =
                    productModel.productPrice.toString()
                showOneProductDetailViewModel?.textViewProductDetailText?.value =
                    productModel.productDescription
            }

            // 첨부 이미지가 있다면
            if (productModel.productMainImage != "none") {
                val work1 = async(Dispatchers.IO) {
                    // 이미지에 접근할 수 있는 uri를 가져온다.
                    SellerService.gettingMainImage(productModel.productMainImage)
                }

                val imageUri = work1.await()
                sellerActivity.showServiceMainImage(
                    imageUri,
                    fragmentShowOneProductDetailBinding.imageViewMainImage
                )
                fragmentShowOneProductDetailBinding.imageViewMainImage.isVisible = true
            }
            Log.d("SubImageLog", "${productModel.productSubImage.isNotEmpty()}")

            // SubImages 표시
            if (productModel.productSubImage.isNotEmpty()) {
                // SubImage가 존재하면 이미지 URIs를 가져온다.
                val subImageUris = SellerService.gettingSubImages(productModel.productSubImage)
                Log.d("SubImageLog", "${productModel.productSubImage}")
                // URI 리스트를 어댑터에 전달
                fragmentShowOneProductDetailBinding.recyclerViewSubImages.layoutManager = LinearLayoutManager(context)
                fragmentShowOneProductDetailBinding.recyclerViewSubImages.adapter = ImageAdapter(subImageUris)
                fragmentShowOneProductDetailBinding.recyclerViewSubImages.isVisible = true
            } else {
                // SubImage가 없을 경우 기본 이미지를 표시
                val defaultImageUri: Uri? = null  // 기본 이미지 표시를 위한 URI (null이면 Glide에서 기본 이미지로 대체)
                val defaultUris = listOf(defaultImageUri)

                // 기본 이미지 URI를 어댑터에 전달
                fragmentShowOneProductDetailBinding.recyclerViewSubImages.layoutManager = LinearLayoutManager(context)
                fragmentShowOneProductDetailBinding.recyclerViewSubImages.adapter = ImageAdapter(defaultUris)
                fragmentShowOneProductDetailBinding.recyclerViewSubImages.isVisible = true
            }
        }
    }

    // 상품 삭제 처리 메서드
    fun proProductDelete(){
        CoroutineScope(Dispatchers.Main).launch {
            // 만약 첨부 이미지가 있다면 삭제한다.
            if(productModel.productName != "none"){
                val work1 = async(Dispatchers.IO){
                    SellerService.removeImageFile(productModel.productMainImage)
                    SellerService.removeImageFiles(productModel.productSubImage)
                }
                work1.join()
            }
            // 글 정보를 삭제한다.
            val work2 = async(Dispatchers.IO){
                SellerService.deleteProductData(productDocumentId)
            }
            work2.join()

            // 글 목록 화면으로 이동한다.
            sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_PRODUCT)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshProductData()
    }

    fun refreshProductData() {
        // 기존 데이터를 초기화
        fragmentShowOneProductDetailBinding.apply {
            showOneProductDetailViewModel?.textProductNameText?.value = ""
            showOneProductDetailViewModel?.textProductCategoryText?.value = ""
            showOneProductDetailViewModel?.textProductPriceText?.value = ""
            showOneProductDetailViewModel?.textViewProductDetailText?.value = ""
            imageViewMainImage.setImageDrawable(null)
            imageViewMainImage.isVisible = false
            recyclerViewSubImages.adapter = null
            recyclerViewSubImages.isVisible = false
        }

        // 새로운 데이터를 다시 불러옴
        settingProductData()
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment() {
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_DETAIL_PRODUCT)
    }

    inner class ImageAdapter(private var imageUris: List<Uri?>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

        // ViewHolder 클래스
        inner class ImageViewHolder(val binding: ItemImageDetailSettingBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val binding = DataBindingUtil.inflate<ItemImageDetailSettingBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_image_detail_setting,
                parent,
                false
            )
            return ImageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageUri = imageUris[position]

            // 이미지가 있으면 Uri로, 없으면 기본 이미지를 표시
            Glide.with(holder.binding.root.context)
                .load(imageUri ?: R.drawable.ic_image_placeholder)  // imageUri가 null이면 기본 이미지를 로드
                .centerCrop()
                .into(holder.binding.imageViewPreview)
        }

        override fun getItemCount(): Int = imageUris.size
    }
}