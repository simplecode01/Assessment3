package com.simplecode01.assessment3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplecode01.assessment3.model.Items
import com.simplecode01.assessment3.network.ApiStatus
import com.simplecode01.assessment3.network.WarehouseApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class AddItemViewModel : ViewModel() {

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var data = mutableStateOf(emptyList<Items>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = WarehouseApi.service.getItem(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("ListItemV", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, nama: String, detail: String, quantity: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = WarehouseApi.service.postItem(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    detail.toRequestBody("text/plain".toMediaTypeOrNull()),
                    quantity.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )
                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("AddItemViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun clearMessage() { errorMessage.value = null }
}