package com.lion.judamie_seller.model

import android.graphics.Bitmap

data class ImageData(val imageUrl: String?, val isMainImage: Boolean, val isDefault: Boolean, var imageBitmap: Bitmap? = null)