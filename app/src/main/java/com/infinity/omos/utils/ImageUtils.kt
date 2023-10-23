package com.infinity.omos.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.infinity.omos.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlin.random.Random

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

fun uriToBitmap(context: Context, uri: Uri?): Bitmap? {
    if (uri == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } else {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }
}

@Composable
fun randomRecordImage(): Painter {
    val images = listOf(
        R.drawable.img_record_1,
        R.drawable.img_record_2,
        R.drawable.img_record_3
    )
    val randomNumber = remember { Random.nextInt(0, images.size) }
    val drawable = images[randomNumber]

    return painterResource(id = drawable)
}