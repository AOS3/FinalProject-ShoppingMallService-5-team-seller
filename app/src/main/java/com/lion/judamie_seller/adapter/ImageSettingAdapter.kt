package com.lion.judamie_seller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lion.judamie_seller.databinding.ItemImageSettingBinding
import com.lion.judamie_seller.model.ImageData

class ImageSettingAdapter(
    private val onRemoveClick: (Int) -> Unit
) : RecyclerView.Adapter<ImageSettingAdapter.ImageViewHolder>() {

    private val imageList = mutableListOf<ImageData>()

    val Items: List<ImageData>
        get() = imageList

    inner class ImageViewHolder(private val binding: ItemImageSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageData: ImageData, position: Int) {
            binding.imageData = imageData
            binding.buttonRemoveImage.setOnClickListener {
                onRemoveClick(position)
            }
            binding.executePendingBindings() // 강제 바인딩
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
        // 디버깅 로그 추가
        println("ADDIMAGE: Image added at position ${imageList.size - 1}, total count: ${imageList.size}")
    }

    fun setImages(newImages: List<ImageData>) {
        imageList.clear()
        imageList.addAll(newImages)
        notifyDataSetChanged() // RecyclerView에 전체 데이터 변경 알림
        // 디버깅 로그 추가
        println("ADDIMAGE: Images replaced, total count: ${imageList.size}")
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
                        imageUrl = "res/drawable/ic_image_placeholder.xml",
                        isMainImage = false,
                        isDefault = true
                    )
                )
            }
        }
    }
}
