package com.infinity.omos.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

@Composable
fun useOpenImagePicker(
    context: Context,
    width: Int,
    height: Int,
    onResult: (Uri) -> Unit
): () -> Unit {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val cropLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    CropImage.getActivityResult(it.data)?.let { cropResult ->
                        val cropImageUri = cropResult.uri
                        onResult(cropImageUri)
                    }
                }

                AppCompatActivity.RESULT_CANCELED -> {}
                else -> {}
            }
        }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                val intent = CropImage
                    .activity(uri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(width, height)
                    .getIntent(context)
                cropLauncher.launch(intent)
            }
        }

    val request =
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
    return { galleryLauncher.launch(request) }
}