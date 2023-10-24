package com.infinity.omos.ui.record.write.writeform

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infinity.omos.R
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.record.write.MusicTopBar
import com.infinity.omos.ui.record.write.RecordTitleBox
import com.infinity.omos.ui.record.write.TextCount
import com.infinity.omos.ui.record.write.WriteRecordViewModel
import com.infinity.omos.ui.theme.black_02
import com.infinity.omos.ui.theme.grey_01
import com.infinity.omos.ui.theme.grey_03
import com.infinity.omos.ui.view.BackIcon
import com.infinity.omos.ui.view.OmosTopAppBar

private const val MAX_TITLE_LENGTH = 36
private const val MAX_CONTENTS_LENGTH = 12

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
    val lyrics = viewModel.lyrics.collectAsState().value

    WriteRecordScreen(
        music = music,
        title = title,
        imageUri = imageUri,
        isPublic = isPublic,
        lyrics = lyrics,
        onBackClick = onBackClick,
        onCompleteClick = onCompleteClick,
        onTitleChange = { viewModel.setTitle(it) },
        onImageChange = { viewModel.setImageUri(it) },
        onLockClick = { viewModel.changeLockState() },
        onContentsChange = { viewModel.setContents(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteRecordScreen(
    music: MusicModel,
    title: String,
    imageUri: Uri?,
    isPublic: Boolean,
    lyrics: String,
    onBackClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onImageChange: (Uri) -> Unit = {},
    onLockClick: () -> Unit = {},
    onContentsChange: (String) -> Unit = {}
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
            modifier = Modifier
                .padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            val lyricsList = lyrics.split("\n")
            val lazyListState = rememberLazyListState()
            val contentsList = Array(lyricsList.size) { remember { mutableStateOf("") } }

            Spacer(
                modifier = Modifier
                    .height(12.dp)
                    .background(color = black_02)
            )
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
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
                }

                itemsIndexed(
                    items = lyricsList
                ) { idx, row ->
                    LyricsItem(
                        modifier = Modifier.fillMaxWidth(),
                        lyricsRow = row,
                        contents = contentsList[idx].value,
                        isLastItem = lyricsList.lastIndex == idx,
                        onContentsChange = {
                            val contentsLength = contentsList.filterIndexed { index, _ ->
                                index != idx
                            }.sumOf { c -> c.value.length } + it.length
                            if (contentsLength <= MAX_CONTENTS_LENGTH) {
                                contentsList[idx].value = it
                                val result = lyricsList.zip(contentsList) { lyricsRow, contents ->
                                    "${lyricsRow}\n${contents.value}"
                                }.joinToString(separator = "\n")
                                onContentsChange(result)
                            }
                        }
                    )
                }
            }
            Divider(
                modifier = Modifier.padding(20.dp),
                thickness = 1.dp
            )
            Row {
                TextCount(
                    modifier = Modifier.padding(horizontal = Dimens.PaddingNormal),
                    name = stringResource(R.string.title),
                    count = title.length,
                    maxLength = MAX_TITLE_LENGTH
                )
                TextCount(
                    modifier = Modifier.padding(horizontal = Dimens.PaddingNormal),
                    name = stringResource(R.string.contents),
                    count = contentsList.sumOf { it.value.length },
                    maxLength = MAX_CONTENTS_LENGTH
                )
            }
            Spacer(modifier = Modifier.height(Dimens.PaddingNormal))
        }
    }
}

@Composable
fun LyricsItem(
    modifier: Modifier,
    lyricsRow: String,
    contents: String,
    isLastItem: Boolean,
    onContentsChange: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = Dimens.PaddingNormal)
    ) {
        Text(
            modifier = modifier,
            text = lyricsRow,
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.64.sp,
                fontFamily = FontFamily(Font(R.font.cafe24oneprettynight)),
                fontWeight = FontWeight(400),
                color = grey_03
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            modifier = modifier,
            value = contents,
            onValueChange = onContentsChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 25.6.sp,
                color = grey_01
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = if (isLastItem) {
                    ImeAction.Done
                } else {
                    ImeAction.Next
                }
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}