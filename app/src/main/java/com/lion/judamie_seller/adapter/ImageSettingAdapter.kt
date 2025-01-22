package com.lion.judamie_seller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lion.judamie_seller.SellerActivity
import com.lion.judamie_seller.databinding.ItemImageSettingBinding
import com.lion.judamie_seller.model.ImageData

class ImageSettingAdapter(
    private val onRemoveClick: (Int) -> Unit,
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
                        imageUrl = "res/drawable/ic_image_placeholder.png",
                        isMainImage = false,
                        isDefault = true
                    )
                )
            }
        }
    }

    fun getMainImageView(recyclerView: RecyclerView): ImageView? {
        // 메인 이미지를 찾기 위해서 이미지 목록을 순회하면서 'isMainImage'가 true인 항목을 반환
        for (imageData in imageList) {
            if (imageData.isMainImage) {
                // Main image가 설정된 경우, 해당 이미지의 ImageView를 반환
                val position = imageList.indexOf(imageData)
                // 해당 position에 있는 ViewHolder를 찾음
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as? ImageViewHolder
                // ViewHolder가 존재하면, 그 안의 imageViewPreview 반환
                return viewHolder?.getImageViewPreview()
            }
        }
        // 기본적으로 메인 이미지가 없다면 null 반환
        return null
    }

    fun getSubImageViews(recyclerView: RecyclerView): List<ImageView> {
        val subImageViews = mutableListOf<ImageView>()

                for (imageData in imageList) {
            if (!imageData.isMainImage) {
                // Main image가 설정된 경우, 해당 이미지의 ImageView를 찾기 위해 position을 가져옴
                val position = imageList.indexOf(imageData)
                // 해당 position에 있는 ViewHolder를 찾음
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as? ImageViewHolder
                // ViewHolder가 존재하면, 그 안의 imageViewPreview를 리스트에 추가
                viewHolder?.getImageViewPreview()?.let { subImageViews.add(it) }
            }
        }

        // 메인 이미지들로 구성된 리스트 반환
        return subImageViews
    }
}
