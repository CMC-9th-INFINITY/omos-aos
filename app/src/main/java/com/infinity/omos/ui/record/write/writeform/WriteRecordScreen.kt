package com.infinity.omos.ui.record.write.writeform

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinity.omos.R
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.record.write.MusicTopBar
import com.infinity.omos.ui.record.write.RecordTitleBox
import com.infinity.omos.ui.record.write.WriteRecordViewModel
import com.infinity.omos.ui.view.BackIcon
import com.infinity.omos.ui.view.OmosTopAppBar

@Composable
fun WriteRecordScreen(
    viewModel: WriteRecordViewModel,
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    val music = viewModel.music.collectAsState().value
    val title = viewModel.title.collectAsState().value
    val imageUri = viewModel.imageUri.collectAsState().value
    val isPublic = viewModel.isPublic.collectAsState().value

    WriteRecordScreen(
        music = music,
        title = title,
        imageUri = imageUri,
        isPublic = isPublic,
        onBackClick = onBackClick,
        onCompleteClick = onCompleteClick,
        onTitleChange = { viewModel.setTitle(it) },
        onImageChange = { viewModel.setImageUri(it) },
        onLockClick = { viewModel.changeLockState() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteRecordScreen(
    music: MusicModel,
    title: String,
    imageUri: Uri?,
    isPublic: Boolean,
    onBackClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onImageChange: (Uri) -> Unit = {},
    onLockClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            OmosTopAppBar(
                title = stringResource(R.string.lyrics),
                navigationIcon = { BackIcon(onClick = onBackClick) },
                actions = {
                    Text(
                        modifier = Modifier
                            .padding(Dimens.PaddingNormal)
                            .clickable { onCompleteClick() },
                        text = stringResource(id = R.string.complete),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

            Spacer(modifier = Modifier.height(12.dp))
            MusicTopBar(music = music)
            RecordTitleBox(
                title = title,
                imageUri = imageUri,
                isPublic = isPublic,
                onTitleChange = onTitleChange,
                onImageChange = onImageChange,
                onLockClick = onLockClick
            )



            Spacer(modifier = Modifier.height(20.dp))
            Divider(
                modifier = Modifier.padding(20.dp),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}