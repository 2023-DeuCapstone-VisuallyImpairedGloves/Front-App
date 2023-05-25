package com.example.deucapstone2023.ui.navigation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deucapstone2023.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor() :ViewModel(){
    private val _bitmapFlow = MutableStateFlow<Bitmap>(Bitmap.createBitmap(50,50,Bitmap.Config.ARGB_8888))
    private val _classificationsFlow = MutableStateFlow<String>("")
    val bitmapFlow get() = _bitmapFlow.asStateFlow()
    val classificationsFlow get() = _classificationsFlow.asStateFlow()
    var bluetoothState = false

    fun emitBitmapFlow(bitmap : Bitmap){
        viewModelScope.launch {
            _bitmapFlow.emit(bitmap)
        }
    }

    fun emitClassificationsFlow(label : String){
        viewModelScope.launch {
            _classificationsFlow.emit(label)
            Log.d("BluetoothTests", label)
        }
    }

}