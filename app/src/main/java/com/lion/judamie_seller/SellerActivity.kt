package com.lion.judamie_seller

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.thread


class SellerActivity : AppCompatActivity() {

    lateinit var activitySellerBinding: ActivitySellerBinding


    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    // 촬영된 사진이 위치할 경로
    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seller)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        filePath = getExternalFilesDir(null).toString()
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

    // 다이얼로그를 통해 메시지를 보여주는 함수
    fun showMessageDialog(title:String, message:String, posTitle:String, callback:()-> Unit){
        val builder = MaterialAlertDialogBuilder(this@SellerActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(posTitle){ dialogInterface: DialogInterface, i: Int ->
            callback()
        }
        builder.show()
    }

    // 키보드 올리는 메서드
    fun showSoftInput(view: View){
        // 입력을 관리하는 매니저
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // 포커스를 준다.
        view.requestFocus()

        thread {
            SystemClock.sleep(500)
            // 키보드를 올린다.
            inputManager.showSoftInput(view, 0)
        }
    }

    fun saveMainImageView(imageView: ImageView?) {
        saveImageView(imageView, "UploadMain")
    }

    fun saveSubImageViews(imageViews: List<ImageView>) {
        // ImageView 리스트 전체를 순회
        imageViews.forEachIndexed { index, imageView ->
            // 이미지 데이터를 추출
            val bitmapDrawable = imageView.drawable as? BitmapDrawable
            val bitmap = bitmapDrawable?.bitmap

            // 만약 비트맵이 null이라면 저장을 건너뛰기
            if (bitmap != null) {
                // 고유한 파일 이름 생성 (이미지 인덱스를 붙여서 저장)
                val fileName = "UploadSub_$index.jpg"
                val file = File("$filePath/$fileName")

                // 파일로 저장
                try {
                    val fileOutputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun saveImageView(imageView: ImageView?, fileName: String){
        // ImageView에서 이미지 데이터를 추출한다.
        val bitmapDrawable = imageView?.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap

        // 저장할 파일의 경로
        val file = File("${filePath}/$fileName.jpg")
        // 파일로 저장한다.
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    }

    // 이미지 뷰에 있는 이미지를 파일로 저장한다.
    fun saveImageView(imageView:ImageView){
        // ImageView에서 이미지 데이터를 추출한다.
        val bitmapDrawable = imageView.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap

        // 저장할 파일의 경로
        val file = File("${filePath}/uploadTemp.jpg")
        // 파일로 저장한다.
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    }
}