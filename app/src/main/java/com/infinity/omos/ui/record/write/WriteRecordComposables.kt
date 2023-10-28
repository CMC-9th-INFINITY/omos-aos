package com.infinity.omos.ui.record.write

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.infinity.omos.BuildConfig
import com.infinity.omos.R
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.grey_01
import com.infinity.omos.ui.theme.grey_02
import com.infinity.omos.ui.theme.grey_03
import com.infinity.omos.ui.theme.white
import com.infinity.omos.utils.DateUtil
import com.infinity.omos.utils.requestPermission
import com.infinity.omos.utils.uriToBitmap
import com.infinity.omos.utils.useOpenImagePicker
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MusicTopBar(
    music: MusicModel
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            GlideImage(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = grey_03),
                imageModel = { music.albumImageUrl },
                failure = {
                    Image(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(id = R.drawable.ic_music),
                        contentDescription = stringResource(id = R.string.music_icon)
                    )
                }
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
                    .weight(1f),
            ) {
                Text(
                    text = music.musicTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = music.artistsAndAlbumTitle,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Image(
                modifier = Modifier
                    .padding(horizontal = Dimens.PaddingNormal)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_spotify),
                contentDescription = stringResource(id = R.string.spotify_icon)
            )
        }
    }
}

@Composable
fun TextCount(
    modifier: Modifier,
    name: String,
    count: Int,
    maxLength: Int
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            fontSize = 14.sp,
            color = grey_03
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${count}/${maxLength}자",
            fontSize = 14.sp,
            color = grey_01
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordTitleBox(
    title: String = "",
    imageUri: Uri? = null,
    defaultImageUrl: String = "",
    isPublic: Boolean = true,
    onTitleChange: (String) -> Unit = {},
    onImageChange: (Uri) -> Unit = {},
    onLockClick: () -> Unit = {}
) {
    val context = LocalContext.current

    var text by remember { mutableStateOf(title) }
    val componentSize = remember { mutableStateOf(Size.Zero) }

    val imagePermissionState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(permission = Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    var uri by remember { mutableStateOf(imageUri) }
    val openImagePicker = useOpenImagePicker(
        context = context,
        width = componentSize.value.width.toInt(),
        height = componentSize.value.height.toInt(),
        onResult = {
            uri = it
            onImageChange(it)
        }
    )

    Box(
        modifier = Modifier
            .height(164.dp)
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                componentSize.value = Size(
                    width = coordinates.size.width.toFloat(),
                    height = coordinates.size.height.toFloat()
                )
            }
    ) {
        val imageBitmap = uriToBitmap(context, uri)?.asImageBitmap()
        if (imageBitmap == null) {
            GlideImage(
                modifier = Modifier.fillMaxSize(),
                imageModel = { BuildConfig.S3_BASE_URL + defaultImageUrl }
            )
        } else {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = imageBitmap,
                contentDescription = ""
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = Dimens.PaddingNormal,
                    vertical = 12.dp
                )
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable {
                        requestPermission(context, imagePermissionState) {
                            openImagePicker()
                        }
                    },
                painter = painterResource(id = R.drawable.ic_gallery),
                contentDescription = "사진 아이콘",
                tint = white
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                val today = DateUtil.convertToUiRecordDate(System.currentTimeMillis())

                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = {
                        if (it.length <= MAX_TITLE_LENGTH) {
                            text = it
                            onTitleChange(it)
                        }
                    },
                    textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W500),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.input_record_title),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W500),
                                color = grey_02
                            )
                        }
                        innerTextField()
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = today,
                    style = MaterialTheme.typography.labelMedium,
                    color = grey_01
                )
            }
            if (isPublic) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clickable { onLockClick() },
                    painter = painterResource(id = R.drawable.ic_public),
                    contentDescription = "자물쇠 열림 아이콘",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Icon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clickable { onLockClick() },
                    painter = painterResource(id = R.drawable.ic_private),
                    contentDescription = "자물쇠 닫힘 아이콘",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun RecordTitleBoxPreview() {
    OmosTheme {
        RecordTitleBox()
    }
}