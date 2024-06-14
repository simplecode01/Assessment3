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
import okhttp3.RequestBody.Companion.toRequestBody

class ListItemViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Items>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = WarehouseApi.service.getItem(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("ListItemViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun deleteData(userId: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = WarehouseApi.service.deleteItem(
                    userId,
                    id
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

}