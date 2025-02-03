package com.lion.judamie_seller.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lion.judamie_seller.databinding.ItemImageSettingBinding
import com.lion.judamie_seller.model.ImageData

class ImageSettingAdapter(
    private val onRemoveClick: (Int) -> Unit,
) : RecyclerView.Adapter<ImageSettingAdapter.ImageViewHolder>() {

    private val imageList = mutableListOf<ImageData>()
    private var isButtonEnabled: Boolean = true

    val Items: List<ImageData>
        get() = imageList

    inner class ImageViewHolder(private val binding: ItemImageSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageData: ImageData, position: Int) {
            binding.imageData = imageData
            binding.buttonRemoveImage.isEnabled = isButtonEnabled

            binding.buttonRemoveImage.setOnClickListener {
                if (isButtonEnabled) {
                    onRemoveClick(position)
                }
            }

            binding.executePendingBindings() // 강제 바인딩
        }

        // binding을 외부에서 접근할 수 있게 public 메서드 추가
        fun getImageViewPreview(): ImageView {
            return binding.imageViewPreview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageSettingBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position], position)
    }

    override fun getItemCount(): Int = imageList.size

    fun addImage(imageData: ImageData) {
        imageList.add(imageData)
        notifyItemInserted(imageList.size - 1)
    }

    fun setImages(newImages: List<ImageData>) {
        imageList.clear()
        imageList.addAll(newImages)
        notifyDataSetChanged() // RecyclerView에 전체 데이터 변경 알림
    }

    // 🔹 버튼 활성화/비활성화 설정 함수 추가
    fun setButtonEnabled(enabled: Boolean) {
        isButtonEnabled = enabled
        notifyDataSetChanged() // 변경 사항 반영
    }

    fun removeImage(position: Int) {
        if (position in imageList.indices) {
            imageList.removeAt(position)
            notifyItemRemoved(position) // 삭제된 위치를 RecyclerView에 알림
            notifyItemRangeChanged(position, imageList.size)

            // 이미지가 모두 삭제된 경우 기본 이미지 추가
            if (imageList.isEmpty()) {
                addImage(
                    ImageData(
                        imageUrl = "res/drawable/ic_image_placeholder.png",
                        isMainImage = false,
                        isDefault = true
                    )
                )
            }
        }
    }

    fun getMainBitmap(): Bitmap? {
        // imageList에서 메인 이미지를 찾아 반환
        for (imageData in imageList) {
            if (imageData.isMainImage) { // isMainImage가 true인 항목 찾기
                return imageData.imageBitmap // 해당 이미지의 Bitmap 반환
            }
        }
        return null // 메인 이미지가 없으면 null 반환
    }

    fun getSubBitmaps(): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()

        // imageList는 어댑터에서 관리하는 실제 데이터 리스트
        for (i in 0 until itemCount) {
            val imageData = imageList[i] // 어댑터의 데이터 리스트에서 아이템 가져오기
            val bitmap = imageData.imageBitmap // ImageData에 저장된 Bitmap (만약 저장되어 있다면)

            // Bitmap이 존재하면 리스트에 추가
            bitmap?.let {
                bitmaps.add(it) // 비트맵만 추가
            }
        }

        return bitmaps
    }
}
