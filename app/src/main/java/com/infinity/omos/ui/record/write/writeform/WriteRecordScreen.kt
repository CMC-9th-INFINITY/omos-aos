package com.infinity.omos.ui.record.write.writeform

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infinity.omos.R
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.record.Category
import com.infinity.omos.ui.record.write.MusicTopBar
import com.infinity.omos.ui.record.write.RecordTitleBox
import com.infinity.omos.ui.record.write.TextCount
import com.infinity.omos.ui.record.write.WriteRecordViewModel
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.black_02
import com.infinity.omos.ui.theme.grey_01
import com.infinity.omos.ui.theme.grey_02
import com.infinity.omos.ui.theme.grey_03
import com.infinity.omos.ui.view.BackIcon
import com.infinity.omos.ui.view.OmosTopAppBar

private const val MAX_TITLE_LENGTH = 36
private const val MAX_LYRICS_CONTENTS_LENGTH = 380
private const val MAX_A_LINE_CONTENTS_LENGTH = 50
private const val MAX_CONTENTS_LENGTH = 164

@Composable
fun WriteRecordScreen(
    viewModel: WriteRecordViewModel,
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    val category = viewModel.category.collectAsState().value
    val music = viewModel.music.collectAsState().value
    val title = viewModel.title.collectAsState().value
    val imageUri = viewModel.imageUri.collectAsState().value
    val isPublic = viewModel.isPublic.collectAsState().value
    val lyrics = viewModel.lyrics.collectAsState().value
    val recordContents = viewModel.contents.collectAsState().value

    WriteRecordScreen(
        category = category,
        music = music,
        title = title,
        imageUri = imageUri,
        isPublic = isPublic,
        lyrics = lyrics,
        recordContents = recordContents,
        onBackClick = onBackClick,
        onCompleteClick = {
            // viewModel.saveRecord()
            onCompleteClick()
        },
        onTitleChange = { viewModel.setTitle(it) },
        onImageChange = { viewModel.setImageUri(it) },
        onLockClick = { viewModel.changeLockState() },
        onContentsChange = { viewModel.setContents(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteRecordScreen(
    category: Category,
    music: MusicModel,
    title: String = "",
    imageUri: Uri? = null,
    isPublic: Boolean = true,
    lyrics: String = "",
    recordContents: String = "",
    onBackClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onImageChange: (Uri) -> Unit = {},
    onLockClick: () -> Unit = {},
    onContentsChange: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val appBarTitle = when (category) {
        Category.A_LINE -> stringResource(id = R.string.a_line)
        Category.STORY -> stringResource(id = R.string.story)
        Category.OST -> stringResource(id = R.string.ost)
        Category.LYRICS -> stringResource(id = R.string.lyrics)
        Category.FREE -> stringResource(id = R.string.free)
    }
    Scaffold(
        topBar = {
            OmosTopAppBar(
                title = appBarTitle,
                navigationIcon = { BackIcon(onClick = onBackClick) },
                actions = {
                    Text(
                        modifier = Modifier
                            .padding(Dimens.PaddingNormal)
                            .clickable {
                                if (title.isEmpty()) {
                                    Toast
                                        .makeText(context, "레코드 제목을 입력하세요.", Toast.LENGTH_SHORT)
                                        .show()
                                } else if (recordContents.isEmpty()) {
                                    Toast
                                        .makeText(context, "레코드 내용을 입력하세요.", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    onCompleteClick()
                                }
                            },
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
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = black_02)
        ) {
            val lazyListState = rememberLazyListState()

            val lyricsList = lyrics.split("\n")
            val contentsList = Array(lyricsList.size) { remember { mutableStateOf("") } }
            var contents by remember { mutableStateOf("") }

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(color = MaterialTheme.colorScheme.background)
                    )
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

                when (category) {
                    Category.A_LINE -> item {
                        ALineBox(
                            modifier = Modifier.fillParentMaxSize(),
                            contents = contents,
                            onContentsChange = {
                                contents = it
                                onContentsChange(it)
                            }
                        )
                    }
                    Category.LYRICS -> itemsIndexed(
                        items = lyricsList
                    ) { idx, row ->
                        LyricsBox(
                            idx = idx,
                            lyricsRow = row,
                            lyricsList = lyricsList,
                            contentsList = contentsList,
                            onContentsChange = onContentsChange
                        )
                    }
                    else -> item {
                        BasicCategoryBox(
                            modifier = Modifier.fillParentMaxSize(),
                            contents = contents,
                            onContentsChange = {
                                contents = it
                                onContentsChange(it)
                            }
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier.padding(20.dp),
                thickness = 1.dp
            )
            Row {
                val (count, maxLength) = when (category) {
                    Category.A_LINE -> contents.length to MAX_A_LINE_CONTENTS_LENGTH
                    Category.LYRICS -> contentsList.sumOf { it.value.length } to MAX_LYRICS_CONTENTS_LENGTH
                    else -> contents.length to MAX_CONTENTS_LENGTH
                }
                TextCount(
                    modifier = Modifier.padding(horizontal = Dimens.PaddingNormal),
                    name = stringResource(R.string.title),
                    count = title.length,
                    maxLength = MAX_TITLE_LENGTH
                )
                TextCount(
                    modifier = Modifier.padding(horizontal = Dimens.PaddingNormal),
                    name = stringResource(R.string.contents),
                    count = count,
                    maxLength = maxLength
                )
            }
            Spacer(modifier = Modifier.height(Dimens.PaddingNormal))
        }
    }
}

@Composable
fun ALineBox(
    modifier: Modifier,
    contents: String,
    onContentsChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier.padding(48.dp),
        value = contents,
        onValueChange = {
            if (it.length <= MAX_A_LINE_CONTENTS_LENGTH) {
                onContentsChange(it)
            }
        },
        textStyle = TextStyle(
            fontSize = 22.sp,
            lineHeight = 29.04.sp,
            fontFamily = FontFamily(Font(R.font.cafe24oneprettynight)),
            fontWeight = FontWeight(400),
            color = grey_01,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            if (contents.isEmpty()) {
                Text(
                    text = stringResource(R.string.a_line_contents_hint),
                    style = TextStyle(
                        fontSize = 22.sp,
                        lineHeight = 29.04.sp,
                        fontFamily = FontFamily(Font(R.font.cafe24oneprettynight)),
                        fontWeight = FontWeight(400),
                        color = grey_02,
                        textAlign = TextAlign.Center
                    )
                )
            }
            innerTextField()
        }
    )
}

@Composable
fun LyricsBox(
    idx: Int,
    lyricsRow: String,
    lyricsList: List<String>,
    contentsList: Array<MutableState<String>>,
    onContentsChange: (String) -> Unit
) {
    LyricsItem(
        modifier = Modifier.fillMaxWidth(),
        lyricsRow = lyricsRow,
        contents = contentsList[idx].value,
        isLastItem = lyricsList.lastIndex == idx,
        onContentsChange = {
            val contentsLength = contentsList.filterIndexed { index, _ ->
                index != idx
            }.sumOf { c -> c.value.length } + it.length
            if (contentsLength <= MAX_LYRICS_CONTENTS_LENGTH) {
                contentsList[idx].value = it
                val result =
                    lyricsList.zip(contentsList) { lyricsRow, contents ->
                        "${lyricsRow}\n${contents.value}"
                    }.joinToString(separator = "\n")
                onContentsChange(result)
            }
        }
    )
}

@Composable
fun BasicCategoryBox(
    modifier: Modifier,
    contents: String,
    onContentsChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier.padding(horizontal = Dimens.PaddingNormal),
        value = contents,
        onValueChange = {
            if (it.length <= MAX_CONTENTS_LENGTH) {
                onContentsChange(it)
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            lineHeight = 25.6.sp,
            color = grey_01
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            if (contents.isEmpty()) {
                Text(
                    text = stringResource(R.string.record_contents_hint),
                    style = MaterialTheme.typography.bodyLarge.copy(color = grey_02)
                )
            }
            innerTextField()
        }
    )
}

@Composable
fun KeywordSelectionBox() {

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

@Preview
@Composable
fun WriteRecordScreenPreview() {
    OmosTheme {
        WriteRecordScreen(
            category = Category.STORY,
            music = MusicModel("", "", "", "", "", "")
        )
    }
}