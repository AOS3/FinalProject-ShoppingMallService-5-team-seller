package com.lion.judamie_seller.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.adapter.ModifySettingAdapter
import com.lion.judamie_seller.databinding.FragmentModifyProductBinding
import com.lion.judamie_seller.model.ImageData
import com.lion.judamie_seller.model.ProductModel
import com.lion.judamie_seller.service.SellerService
import com.lion.judamie_seller.util.SellerFragmentType
import com.lion.judamie_seller.viewmodel.ModifyProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ModifyProductFragment() : Fragment() {

    lateinit var fragmentModifyProductBinding: FragmentModifyProductBinding
    private lateinit var sellerActivity: SellerActivity

    // ViewModel 초기화
    private val ModifyProductViewModel: ModifyProductViewModel by lazy {
        ModifyProductViewModel(this@ModifyProductFragment)
    }

    lateinit var productModel: ProductModel

    // 현재 글의 문서 id를 담을 변수
    lateinit var productDocumentId:String

    var isSetImageView = false

    var SubImageNum = 0
    // 원본 글에 이미지가 있는가..
    var isHasBitmap = false
    // 메인 이미지안가
    var isMainBitmap = false

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var isMainImage = false

    var categoryName: String? = null


    lateinit var mainImagesAdapter: ModifySettingAdapter
    lateinit var subImagesAdapter: ModifySettingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 데이터 바인딩 설정
        fragmentModifyProductBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_product, container, false)
        fragmentModifyProductBinding.modifyProductViewModel = ModifyProductViewModel(this@ModifyProductFragment)
        fragmentModifyProductBinding.lifecycleOwner = this@ModifyProductFragment

        sellerActivity = activity as SellerActivity

        categoryName = arguments?.getString("categoryName")

        gettingArguments()

        settingToolbar()

        settingBoardData()
        // RecyclerView 초기화
        setupRecyclerViews()
        // Spinner 초기화
        setupCategorySpinner()

        fragmentModifyProductBinding.recyclerViewMainImages.adapter = mainImagesAdapter
        fragmentModifyProductBinding.recyclerViewSubImages.adapter = subImagesAdapter

        setupImagePicker()

        return fragmentModifyProductBinding.root
    }

    // arguments의 값을 변수에 담아준다.
    fun gettingArguments(){
        productDocumentId = arguments?.getString("productDocumentId")!!
    }

    private fun setupRecyclerViews() {
        // 메인 이미지 RecyclerView 설정
        mainImagesAdapter = ModifySettingAdapter(
            onRemoveClick = { position -> mainImagesAdapter.removeImage(position) },
            viewModel = ModifyProductViewModel
        )


        fragmentModifyProductBinding.recyclerViewMainImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mainImagesAdapter
        }

        // 초기 대표 이미지 추가
        mainImagesAdapter.addImage(ImageData(imageUrl = "res/drawable/ic_image_placeholder.png", isMainImage = true, isDefault = true))

        // 서브 이미지 RecyclerView 설정
        subImagesAdapter = ModifySettingAdapter(
            onRemoveClick = { position -> subImagesAdapter.removeImage(position) },
            viewModel = ModifyProductViewModel
        )

        fragmentModifyProductBinding.recyclerViewSubImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = subImagesAdapter
        }

        // 대표 이미지 추가 버튼
        fragmentModifyProductBinding.buttonModifyMainImage.setOnClickListener {
            isMainImage = true
            openGallery()
        }

        // 추가 이미지 추가 버튼
        fragmentModifyProductBinding.buttonModifyAdditionalImage.setOnClickListener {
            isMainImage = false
            openGallery()
        }
    }

    fun settingBoardData() {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                SellerService.selectProductDataOneById(productDocumentId)
            }
            productModel = work1.await()

            fragmentModifyProductBinding.apply {
                modifyProductViewModel?.textFieldProductNameEditTextText?.value = productModel.productName
                modifyProductViewModel?.textFieldProductPriceEditTextText?.value = productModel.productPrice.toString()
                modifyProductViewModel?.textFieldProductDiscountRateEditTextText?.value = productModel.productDiscountRate.toString()
                modifyProductViewModel?.textFieldProductStockEditTextText?.value = productModel.productStock.toString()
                modifyProductViewModel?.textFieldProductDescriptionEditTextText?.value = productModel.productDescription
                SubImageNum = productModel.productSubImage.size - 1
            }

            // 첨부 이미지가 있다면
            if(productModel.productMainImage != "none"){
                val work1 = async(Dispatchers.IO) {
                    // 이미지에 접근할 수 있는 uri를 가져온다.
                    SellerService.gettingMainImage(productModel.productMainImage)
                }

                val imageUri = work1.await()
                val recyclerMainView = fragmentModifyProductBinding.recyclerViewMainImages
                val mainBitmap = mainImagesAdapter.getMainBitmap()

                //TODO
                sellerActivity.showServiceMainBitmap(
                    mainBitmap!!,
                    recyclerMainView.findViewById(R.id.recyclerViewMainImages)
                )

                // 글에 이미지가 있는지...
                isHasBitmap = true

                // 이미지를 보여준다.
                fragmentModifyProductBinding.buttonModifyMainImage.isVisible = true
            }
            // 서브 이미지 처리
            if (productModel.productSubImage.isNotEmpty()) {
                val work1 = async(Dispatchers.IO) {
                    // 이미지에 접근할 수 있는 uri를 가져옵니다.
                    SellerService.gettingSubImages(productModel.productSubImage)
                }
                // 이미지 Uri 배열을 가져옵니다.
                val imageUri = work1.await()

                // 서브 이미지가 없다면 기본 이미지 홀더 추가
                if (subImagesAdapter.Items.isEmpty()) {
                    subImagesAdapter.addImage(ImageData(imageUrl = "res/drawable/ic_image_placeholder.png", isMainImage = false, isDefault = true))
                }

                // 기존 이미지들 추가 (중복 추가 방지)
                for (index in 0 until productModel.productSubImage.size) {
                    val url = imageUri[index]

                    // 이미 추가된 이미지가 아닌 경우에만 추가
                    val isAlreadyAdded = subImagesAdapter.Items.any { it.imageUrl == url.toString() }
                    if (!isAlreadyAdded) {
                        subImagesAdapter.addImage(ImageData(imageUrl = url.toString(), isMainImage = false, isDefault = false))
                    }
                }
            } else {
                // 서브 이미지가 없으면 기본 이미지 홀더만 추가
                if (subImagesAdapter.Items.isEmpty()) {
                    subImagesAdapter.addImage(ImageData(imageUrl = "res/drawable/ic_image_placeholder.png", isMainImage = false, isDefault = true))
                }
            }
            ModifyProductViewModel.isModifyBitmap = false
            ModifyProductViewModel.isRemoveVitmap = false
            // 이미지 삭제 버튼을 보여준다.
            fragmentModifyProductBinding.buttonModifyAdditionalImage.isVisible = true
        }
    }

    private fun setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
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
                                        isMainImage = true,
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
                        }
                        ModifyProductViewModel.isModifyBitmap = true
                    }
                }
            }
        }
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentModifyProductBinding.apply {
            toolbar.title = "상품 수정"
            toolbar.inflateMenu(R.menu.menu_product)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemProductDone-> {
                        proSellerModifySubmit()
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
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_MODIFY_PRODUCT)
        ModifyProductViewModel.isModifyBitmap = false
        ModifyProductViewModel.isRemoveVitmap = false
    }

    private fun setupCategorySpinner() {
        val spinner = fragmentModifyProductBinding.spinnerCategory

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
                ModifyProductViewModel.productCategory.value = selectedCategory
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무것도 선택하지 않을 경우 처리
            }
        }
    }

    // 글 수정 완료 처리 메서드
    fun proSellerModifySubmit() {
        fragmentModifyProductBinding.apply {
            productModel.productName = modifyProductViewModel?.textFieldProductNameEditTextText?.value!!
            productModel.productPrice = modifyProductViewModel?.textFieldProductPriceEditTextText?.value?.toIntOrNull() ?: 0
            productModel.productDiscountRate = modifyProductViewModel?.textFieldProductDiscountRateEditTextText?.value?.toIntOrNull() ?: 0
            productModel.productStock = modifyProductViewModel?.textFieldProductStockEditTextText?.value?.toIntOrNull() ?: 0
            productModel.productDescription = modifyProductViewModel?.textFieldProductDescriptionEditTextText?.value!!

            CoroutineScope(Dispatchers.Main).launch {
                // 로딩 인디케이터 표시 및 UI 비활성화
                showLoadingIndicator(true)
                setUIEnabled(false)

                if (isHasBitmap) {
                    // 만약 이미지를 삭제했다면
                    if (ModifyProductViewModel.isRemoveVitmap) {
                        if (isMainBitmap) {
                            // 이미지 파일을 삭제한다.
                            val work1 = async(Dispatchers.IO) {
                                SellerService.removeImageFile(productModel.productMainImage)
                            }
                            work1.join()
                            productModel.productMainImage = "none"
                        } else {
                            val work1 = async(Dispatchers.IO) {
                                val checkSub = checkSubImageRemoveIndex(SubImageNum)
                                if (checkSub.isNotEmpty()) {
                                    for (index in checkSub) {
                                        val fileName =
                                            "sub_image_${productModel.productDocumentId}_$index"
                                        SellerService.removeImageFile(fileName)
                                    }
                                    // 삭제된 파일의 인덱스를 제외한 새로운 리스트 생성 및 이름 변경
                                    val remainingSubImageList = productModel.productSubImage
                                        .filterIndexed { index, _ ->
                                            index !in checkSub // checkSub에 포함되지 않는 인덱스만 필터링
                                        }
                                        .mapIndexed { index, _ ->
                                            // 새로운 이름 형식으로 변환: UploadSub_$index.jpg
                                            "UploadSub_$index.jpg"
                                        }
                                        .toMutableList() // MutableList로 변환

                                    // 남은 리스트를 다시 정의
                                    productModel.productSubImage = remainingSubImageList
                                }
                            }
                            work1.join()
                        }
                    }

                    // 이미지를 수정한적이 있다면
                    if (ModifyProductViewModel.isModifyBitmap) {
                        if (isMainBitmap) {
                            if (productModel.productMainImage != "none") {
                                // 이미지 파일을 삭제한다.
                                val work1 = async(Dispatchers.IO) {
                                    SellerService.removeImageFile(productModel.productMainImage)
                                }
                                work1.join()
                            }
                            val recyclerViewMainImages =
                                fragmentModifyProductBinding.recyclerViewMainImages
                            // 서버상에서의 파일 이름
                            productModel.productMainImage =
                                "main_image_${productModel.productDocumentId}.jpg"
                            // 로컬에 ImageView에 있는 이미지 데이터를 저장한다.
                            mainImagesAdapter.getMainBitmap()
                                ?.let { sellerActivity.saveMainBitmap(it) }

                            val work2 = async(Dispatchers.IO) {
                                SellerService.uploadImage(
                                    "${sellerActivity.filePath}/uploadSub_index.jpg",
                                    productModel.productMainImage, true
                                )
                            }
                            work2.join()
                        } else {
                            if (productModel.productSubImage.isNotEmpty()) {
                                val checkSub = checkAddSubImageIndex(SubImageNum)
                                if (checkSub.isNotEmpty()) {
                                    val work1 = async(Dispatchers.IO) {
                                        if (checkSub.isNotEmpty() || productModel.productSubImage.isEmpty()) {
                                            val work2 = async(Dispatchers.IO) {
                                                // 기존 이미지들부터 새로 이름을 설정
                                                val allImages = mutableListOf<String>()

                                                // 기존 이미지를 새 이름으로 갱신
                                                for (i in 0 until productModel.productSubImage.size) {
                                                    val updatedFileName = "sub_image_${productModel.productDocumentId}_$i"
                                                    allImages.add(updatedFileName)
                                                }

                                                // 추가된 이미지 처리: 기존 리스트의 마지막 인덱스 이후로 추가
                                                val startIndex = productModel.productSubImage.size  // 기존 리스트의 끝 인덱스

                                                // 추가된 인덱스에 대해서만 이미지 업로드
                                                for (maxIndex in checkSub) {
                                                    for (index in 0..maxIndex) {
                                                        val fileIndex = startIndex + index
                                                        val imageView = subImagesAdapter.getSubImageViews(recyclerViewSubImages)
                                                        val newFileName =
                                                            "sub_image_${productModel.productDocumentId}_$fileIndex"
                                                        // 이미지 저장
                                                        sellerActivity.saveModifySubImageViews(imageView, productModel.productSubImage.size)
                                                        SellerService.uploadImage(
                                                            "${sellerActivity.filePath}/uploadSub_$fileIndex.jpg",
                                                            newFileName, false
                                                        )
                                                        Log.d("checkModify", "$newFileName")
                                                        // 새 이미지 이름을 리스트에 추가
                                                        allImages.add(newFileName)
                                                    }

                                                    // 파일 이름을 업데이트한 전체 리스트로 productSubImage 갱신
                                                    productModel.productSubImage = allImages
                                                }
                                            }
                                            work2.await()  // 작업 완료까지 기다림
                                        }
                                    }
                                    work1.await()
                                }
                            }
                        }
                    }
                    // 글 데이터를 업로드한다.
                    val work3 = async(Dispatchers.IO){
                        SellerService.updateProductData(productModel)
                    }
                    work3.join()

                    sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_MODIFY_PRODUCT)
                }
                // 로딩 인디케이터 숨기기 및 UI 활성화
                showLoadingIndicator(false)
                setUIEnabled(true)
            }
        }
    }

    suspend fun checkSubImageRemoveIndex(index: Int): List<Int> {
        // 서버에서 서브 이미지 리스트를 가져옵니다.
        val subImageList = SellerService.gettingSubImages(productModel.productSubImage)

        // 0부터 index-1까지 범위에서 존재하지 않는 인덱스를 확인
        return (0 until index).filter { it !in subImageList.indices }
    }

    suspend fun checkAddSubImageIndex(index: Int): List<Int> {
        // 서버에서 서브 이미지 리스트를 가져옵니다.
        val subImageList = SellerService.gettingSubImages(productModel.productSubImage)

        // 기존 리스트의 인덱스 범위
        val existingIndices = subImageList.indices.toSet()

        // index 미만의 정상 범위를 벗어난 추가된 인덱스 찾기
        return existingIndices.filter { it >= index }.sorted()
    }

    fun showLoadingIndicator(show: Boolean) {
        fragmentModifyProductBinding.loadingIndicator.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setUIEnabled(enabled: Boolean) {
        fragmentModifyProductBinding.apply {
            buttonModifyMainImage.isEnabled = enabled
            buttonModifyAdditionalImage.isEnabled = enabled
            textInputProductName.isEnabled = enabled
            textInputPrice.isEnabled = enabled
            textInputDiscount.isEnabled = enabled
            textInputStock.isEnabled = enabled
            textInputDescription.isEnabled = enabled
        }
    }
}
