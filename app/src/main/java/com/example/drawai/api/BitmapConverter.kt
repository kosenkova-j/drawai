package com.example.drawai.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class BitmapConverter {
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArray = ByteArrayOutputStream().apply {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
        }.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun bitmapToMultipart(bitmap: Bitmap, name: String): MultipartBody.Part {
        val byteArray = bitmapToByteArray(bitmap)
        val requestFile = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, "image.png", requestFile)
    }

    fun base64ToBitmap(base64: String): Bitmap {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        return ByteArrayOutputStream().apply {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
        }.toByteArray()
    }
}