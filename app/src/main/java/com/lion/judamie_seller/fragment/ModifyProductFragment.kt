package com.lion.judamie_seller.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.judamie_seller.R
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.adapter.ImageSettingAdapter
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

    private lateinit var fragmentModifyProductBinding: FragmentModifyProductBinding
    private lateinit var sellerActivity: SellerActivity
    // ViewModel 초기화
    private val ModifyProductViewModel: ModifyProductViewModel by lazy {
        ModifyProductViewModel(this@ModifyProductFragment)
    }

    lateinit var productModel: ProductModel

    // 현재 글의 문서 id를 담을 변수
    lateinit var productDocumentId:String

    var isSetImageView = false
    lateinit var productBitmap: Bitmap

    // 원본 글에 이미지가 있는가..
    var isHasBitmap = false
    // 이미지를 사용자가 삭제하였는가..
    var isRemoveBitmap = false
    // 이미지를 수정하였는가..
    var isModifyBitmap = false

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
        mainImagesAdapter = ImageSettingAdapter(
            onRemoveClick = { position -> mainImagesAdapter.removeImage(position) },
        )


        fragmentModifyProductBinding.recyclerViewMainImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mainImagesAdapter
        }

        // 초기 대표 이미지 추가
        mainImagesAdapter.addImage(ImageData(imageUrl = "res/drawable/ic_image_placeholder.png", isMainImage = true, isDefault = true))

        // 서브 이미지 RecyclerView 설정
        subImagesAdapter = ImageSettingAdapter(
            onRemoveClick = { position -> subImagesAdapter.removeImage(position) },
        )

        fragmentModifyProductBinding.recyclerViewSubImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = subImagesAdapter
        }

        // 초기 추가 이미지 추가
        subImagesAdapter.addImage(ImageData(imageUrl = "res/drawable/ic_image_placeholder.png", isMainImage = false, isDefault = true))

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
            }

            // 첨부 이미지가 있다면
            if(productModel.productMainImage != "none"){
                val work1 = async(Dispatchers.IO) {
                    // 이미지에 접근할 수 있는 uri를 가져온다.
                    SellerService.gettingMainImage(productModel.productMainImage)
                }

                val imageUri = work1.await()
                val recyclerMainView = fragmentModifyProductBinding.recyclerViewMainImages

                sellerActivity.showServiceMainImage(
                    imageUri,
                    mainImagesAdapter.getMainImageView(recyclerMainView)!!
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
                            println("ADDIMAGE: Sub image added, total items: ${subImagesAdapter.Items.size}")
                        }
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
        sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_MODIFY_PRODUCT)
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

    // 이미지 뷰에 있는 이미지를 변수에 담아준다.
    fun getBitmapFromImageView(){
        if(::productBitmap.isInitialized == false){
            val recyclerView = fragmentModifyProductBinding.recyclerViewMainImages

            val bitmapDrawable = mainImagesAdapter.getMainImageView(recyclerView)?.drawable as BitmapDrawable
            productBitmap = bitmapDrawable.bitmap
        }
    }

    // 글 작성 완료 처리 메서드
    fun proSellerAddSubmit(){
        fragmentModifyProductBinding.apply {
            var imageName = ""

            var productName = ModifyProductViewModel?.textFieldProductNameEditTextText?.value!!

            var productCategoryText = fragmentModifyProductBinding.spinnerCategory.selectedItem.toString()

            var productPrice = ModifyProductViewModel?.textFieldProductPriceEditTextText?.value!!

            var productDiscount = ModifyProductViewModel?.textFieldProductDiscountRateEditTextText?.value!!

            var productStock = ModifyProductViewModel?.textFieldProductStockEditTextText?.value!!

            var productDescription = ModifyProductViewModel?.textFieldProductDescriptionEditTextText?.value!!
            // 첨부 사진 파일 이름
            var productMainFileName = "none"

            var productSubFileName = "none"
            // 시간
            var productTimeStamp = System.nanoTime()

            if(productName.isEmpty()){
                sellerActivity.showMessageDialog("입력 오류", "제목을 입력해주세요", "확인"){
                    sellerActivity.showSoftInput(textInputProductName.editText!!)
                }
                return
            }

            if(productCategoryText.isEmpty()){
                sellerActivity.showMessageDialog("입력 오류", "내용을 입력해주세요", "확인"){
                    sellerActivity.showSoftInput(spinnerCategory)
                }
                return
            }

            if(productPrice.isEmpty()){
                sellerActivity.showMessageDialog("입력 오류", "내용을 입력해주세요", "확인"){
                    sellerActivity.showSoftInput(textInputPrice.editText!!)
                }
                return
            }

            if(productDiscount.isEmpty()){
                sellerActivity.showMessageDialog("입력 오류", "내용을 입력해주세요", "확인"){
                    sellerActivity.showSoftInput(textInputDiscount.editText!!)
                }
                return
            }

            if(productStock.isEmpty()){
                sellerActivity.showMessageDialog("입력 오류", "내용을 입력해주세요", "확인"){
                    sellerActivity.showSoftInput(textInputStock.editText!!)
                }
                return
            }

            // 업로드
            CoroutineScope(Dispatchers.Main).launch {
                if(isHasBitmap) {
                    // 만약 이미지를 삭제했다면
                    if (isRemoveBitmap) {
                        // 이미지 파일을 삭제한다.
                        val work1 = async(Dispatchers.IO) {
                            SellerService.removeImageFile(productModel.productName)
                        }
                        work1.join()
                        productModel.productMainImage = "none"
                    }
                }
                // 이미지를 수정한적이 있다면
                if(isModifyBitmap){
                    if(productModel.productMainImage != "none") {
                        // 이미지 파일을 삭제한다.
                        val work1 = async(Dispatchers.IO) {
                            SellerService.removeImageFile(productModel.productMainImage)
                        }
                        work1.join()
                    }

                    // 서버상에서의 파일 이름
                    productModel.productMainImage = "image_${System.currentTimeMillis()}.jpg"

                    val imageView = mainImagesAdapter.getMainImageView(fragmentModifyProductBinding.recyclerViewMainImages)
                    if (imageView != null) {
                        sellerActivity.saveImageView(imageView, productModel.productMainImage)
                    } else {
                        println("ImageView가 null입니다.")
                    }

                    val work2 = async(Dispatchers.IO){
                        SellerService.uploadImage("${sellerActivity.filePath}/uploadTemp.jpg", productModel.productMainImage)
                    }
                    work2.join()
                }

                // 글 데이터를 업로드한다.
                val work3 = async(Dispatchers.IO){
                    SellerService.updateProductData(productModel)
                }
                work3.join()

                sellerActivity.removeFragment(SellerFragmentType.SELLER_TYPE_MODIFY_PRODUCT)
            }
        }
    }
}
