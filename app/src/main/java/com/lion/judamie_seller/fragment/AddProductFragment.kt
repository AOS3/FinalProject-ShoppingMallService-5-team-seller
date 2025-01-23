package com.lion.judamie_seller.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.adapter.ImageSettingAdapter
import com.lion.judamie_seller.databinding.FragmentAddProductBinding
import com.lion.judamie_seller.model.ImageData
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.ProductType
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.AddProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddProductFragment() : Fragment() {

    private lateinit var fragmentAddProductBinding: FragmentAddProductBinding
    private lateinit var sellerActivity: SellerActivity
    // ViewModel 초기화
    private val addProductViewModel: AddProductViewModel by lazy {
        AddProductViewModel(this@AddProductFragment)
    }

    var isSetImageView = false

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var isMainImage = false

    var categoryName: String? = null


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

        categoryName = arguments?.getString("categoryName")

        settingToolbar()
        // RecyclerView 초기화
        setupRecyclerViews()
        // Spinner 초기화
        setupCategorySpinner()

        fragmentAddProductBinding.recyclerViewMainImages.adapter = mainImagesAdapter
        fragmentAddProductBinding.recyclerViewSubImages.adapter = subImagesAdapter

        setupImagePicker()

        return fragmentAddProductBinding.root
    }

    private fun setupRecyclerViews() {
        // 메인 이미지 RecyclerView 설정
        mainImagesAdapter = ImageSettingAdapter(
            onRemoveClick = { position -> mainImagesAdapter.removeImage(position) },
        )


        fragmentAddProductBinding.recyclerViewMainImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mainImagesAdapter
        }

        // 초기 대표 이미지 추가
        mainImagesAdapter.addImage(ImageData(imageUrl = "res/drawable/ic_image_placeholder.png", isMainImage = true, isDefault = true))

        // 서브 이미지 RecyclerView 설정
        subImagesAdapter = ImageSettingAdapter(
            onRemoveClick = { position -> subImagesAdapter.removeImage(position) },
        )

        fragmentAddProductBinding.recyclerViewSubImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = subImagesAdapter
        }

        // 초기 추가 이미지 추가
        subImagesAdapter.addImage(ImageData(imageUrl = "res/drawable/ic_image_placeholder.png", isMainImage = false, isDefault = true))

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
                        isSetImageView = true
                    } else {
                        if (subImagesAdapter.Items.any { it.isDefault })  {
                            subImagesAdapter.setImages(
                                listOf(
                                    ImageData(
                                        imageUrl = imageUri.toString(),
                                        isMainImage = false,
                                        isDefault = false
                                    )
                                )
                            )
                            isSetImageView = true
                        } else {
                            subImagesAdapter.addImage(
                                ImageData(
                                    imageUrl = imageUri.toString(),
                                    isMainImage = false,
                                    isDefault = false
                                )
                            )
                            isSetImageView = true
                            // 디버깅 로그 추가
                            println("ADDIMAGE: Sub image added, total items: ${subImagesAdapter.Items.size}")
                        }
                    }
                }
            }
        }
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentAddProductBinding.apply {
            toolbar.title = "상품추가"
            toolbar.inflateMenu(R.menu.menu_product)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemProductDone-> {
                        proSellerAddSubmit()
                    }

                }
                true
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

    private fun setupCategorySpinner() {
        val spinner = fragmentAddProductBinding.spinnerCategory

        // 직접 설정한 카테고리 데이터
        val categories = listOf("와인", "위스키", "보드카", "데낄라", "우리술", "사케", "럼", "리큐르", "중국술", "브랜디", "맥주", "논알콜")

        // Spinner 어댑터 설정
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        // 기본 선택 이벤트 처리
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 선택된 카테고리를 ViewModel에 저장
                val selectedCategory = categories[position]
                addProductViewModel.productCategory.value = selectedCategory
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무것도 선택하지 않을 경우 처리
            }
        }
    }

    // 글 작성 완료 처리 메서드
    fun proSellerAddSubmit(){
        fragmentAddProductBinding.apply {
            var imageName = ""

            val productName = addProductViewModel?.textFieldProductNameEditTextText?.value!!

            val productCategoryText = fragmentAddProductBinding.spinnerCategory.selectedItem.toString()

            val productPrice = addProductViewModel?.textFieldProductPriceEditTextText?.value!!

            val productDiscount = addProductViewModel?.textFieldProductDiscountRateEditTextText?.value!!

            val productStock = addProductViewModel?.textFieldProductStockEditTextText?.value!!

            val productDescription = addProductViewModel?.textFieldProductDescriptionEditTextText?.value!!
            // 첨부 사진 파일 이름
            var productMainFileName = "none"

            var productSubFileName = "none"

            val productSubFileNames = mutableListOf<String>()
            // 시간
            val productTimeStamp = System.nanoTime()

            if(productName.isEmpty()){
                sellerActivity.showMessageDialog("입력 오류", "제목을 입력해주세요", "확인"){
                    sellerActivity.showSoftInput(textInputProductName.editText!!)
                }
                return
            }

            if(productPrice.isEmpty()){
                sellerActivity.showMessageDialog("입력 오류", "가격을 입력해주세요", "확인"){
                    sellerActivity.showSoftInput(textInputPrice.editText!!)
                }
                return
            }

            if(productDiscount.toInt() < 0 || productDiscount.toInt() >= 100){
                sellerActivity.showMessageDialog("입력 오류", "할인율이 0보다 작거나 100을 넘습니다.", "확인"){
                    sellerActivity.showSoftInput(textInputPrice.editText!!)
                }
                return
            }

            if(productDiscount.isEmpty()){
                sellerActivity.showMessageDialog("입력 오류", "할인율을 입력해주세요", "확인"){
                    sellerActivity.showSoftInput(textInputDiscount.editText!!)
                }
                return
            }

            if(productStock.isEmpty()){
                sellerActivity.showMessageDialog("입력 오류", "재고량을 입력해주세요", "확인"){
                    sellerActivity.showSoftInput(textInputStock.editText!!)
                }
                return
            }

            if(productMainFileName == "none" && productSubFileName != "none") {
                sellerActivity.showMessageDialog("입력 오류", "메인 이미지를 먼저 등록해주세요.", "확인"){
                    sellerActivity.showSoftInput(textInputStock.editText!!)
                }
                return
            }

            // 업로드
            CoroutineScope(Dispatchers.Main).launch {
                // 이미지가 첨부되어 있다면
                if(isSetImageView) {
                    val productModel = ProductModel()
                    // 서버상에서의 파일 이름
                    productMainFileName = "main_image_$productName.jpg"
                    // 로컬에 ImageView에 있는 이미지 데이터를 저장한다.
                    mainImagesAdapter.getMainImageView(recyclerViewMainImages)
                        ?.let { sellerActivity.saveMainImageView(it) }

                    val imageView = subImagesAdapter.getSubImageViews(recyclerViewSubImages)
                    val subImageCount = subImagesAdapter.itemCount
                    // 서브 이미지 파일 이름을 리스트로 관리
                    for (index in 0 until subImageCount) {
                        // 고유한 파일 이름 생성
                        productSubFileName =
                            "sub_image_${productName}_${index}.jpg"

                        // 이미지 저장
                        sellerActivity.saveSubImageViews(imageView)

                        // 서브 이미지 파일 이름을 리스트에 추가
                        productSubFileNames.add(productSubFileName)
                    }


                    // 이미지 업로드
                    val work1 = async(Dispatchers.IO) {
                        SellerService.uploadImage(
                            "${sellerActivity.filePath}/uploadMain.jpg",
                            productMainFileName
                        )
                        productSubFileNames.forEachIndexed() { index, productSubFileName ->
                            val uniqueFileName = "${sellerActivity.filePath}/uploadSub_${index}.jpg"
                            SellerService.uploadImage(uniqueFileName, productSubFileName)
                            Log.d("UploadSub Success", uniqueFileName)
                        }
                    }
                    work1.join()
                }

                val currentDate = System.currentTimeMillis()

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(currentDate))

                // 서버에 저장할 글 데이터
                val productModel = ProductModel()
                productModel.productName = productName
                productModel.productCategory = productCategoryText
                productModel.productPrice = productPrice.toInt()
                productModel.productDiscountRate = productDiscount.toInt()
                productModel.productStock = productStock.toInt()
                productModel.productDescription = productDescription
                productModel.productMainImage = productMainFileName
                productModel.productSubImage = productSubFileNames
                productModel.productRegisterDate = formattedDate
                productModel.productTimeStamp = productTimeStamp
                // 저장한다.
                val work2 = async(Dispatchers.IO){
                    SellerService.addProductData(productModel)
                }
                val documentId = work2.await()
                // 글을 보는 화면으로 이동한다.
                // 문서의 아이디를 전달한다.
                val dataBundle = Bundle()
                dataBundle.putString("productDocumentId", documentId)
                sellerActivity.replaceFragment(SellerFragmentType.SELLER_TYPE_PRODUCT_MANAGEMENT, true, true, dataBundle)
            }
        }
    }
}
