package com.infinity.omos.ui.record.write

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.infinity.omos.ui.record.MusicTopBar
import com.infinity.omos.ui.record.RecordForm
import com.infinity.omos.ui.record.RecordTitleBox
import com.infinity.omos.ui.record.TextCount
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.black_02
import com.infinity.omos.ui.theme.chip
import com.infinity.omos.ui.theme.grey_01
import com.infinity.omos.ui.theme.grey_02
import com.infinity.omos.ui.theme.grey_03
import com.infinity.omos.ui.theme.roundedField
import com.infinity.omos.ui.view.BackIcon
import com.infinity.omos.ui.view.OmosTopAppBar

const val MAX_TITLE_LENGTH = 36
private const val MAX_LYRICS_CONTENTS_LENGTH = 380
private const val MAX_A_LINE_CONTENTS_LENGTH = 50
private const val MAX_CONTENTS_LENGTH = 164
private const val MAX_ONE_LAST_CONTENTS_LENGTH = 20

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
    val recordForm = viewModel.recordForm.collectAsState().value
    val defaultImageUrl = viewModel.recordImageUrl.collectAsState().value

    WriteRecordScreen(
        category = category,
        music = music,
        title = title,
        imageUri = imageUri,
        isPublic = isPublic,
        defaultImageUrl = defaultImageUrl,
        lyrics = lyrics,
        recordContents = recordContents,
        recordForm = recordForm,
        onBackClick = onBackClick,
        onCompleteClick = {
            // viewModel.saveRecord() TODO: 레코드 저장 로직 활성화
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
    defaultImageUrl: String = "",
    isPublic: Boolean = true,
    lyrics: String = "",
    recordContents: String = "",
    recordForm: RecordForm = RecordForm.WRITE,
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
                        defaultImageUrl = defaultImageUrl,
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

                    else -> {
                        if (recordForm != RecordForm.KEYWORD) {
                            item {
                                BasicCategoryBox(
                                    modifier = Modifier.fillParentMaxSize(),
                                    contents = contents,
                                    onContentsChange = {
                                        contents = it
                                        onContentsChange(it)
                                    }
                                )
                            }
                        } else {
                            item {
                                KeywordSelectionBox(
                                    category = category
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Divider(
                modifier = Modifier.padding(
                    bottom = 20.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
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
                if (recordForm == RecordForm.WRITE) {
                    TextCount(
                        modifier = Modifier.padding(horizontal = Dimens.PaddingNormal),
                        name = stringResource(R.string.contents),
                        count = count,
                        maxLength = maxLength
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeywordSelectionBox(
    modifier: Modifier = Modifier,
    contents: String = "",
    category: Category = Category.OST,
    onContentsChange: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf(contents) }
    val (question1, chipList1) = "Q. 이 노래의 매력 3가지를 꼽는다면?" to listOf(
        "🍯 음색", "🥁 비트", "🎬 도입부",
        "🎸 밴드 사운드", "💃🏻‍ 퍼포먼스", "🎼 멜로디",
        "✍🏼‍ 가사", "👑 비주얼", "🎤 랩"
    )
    val (question2, chipList2) = "Q. 이 노래를 들었을 때 어떤 감정들이 느껴졌나요?" to listOf(
        "🥰 행복", "💗 설렘", "😆 신남", "🧙🏼‍♀️️ 몽환",
        "😢 슬픔", "😔 우울", "🍂 그리움",
        "🥺️️ 외로움", "🫂️️ 따뜻함", "☺️ 아련함",
        "🧘🏻‍♀️️️ 평화로움", "🥲️️ 감동적인"
    )
    val (question3, chipList3) = when (category) {
        Category.OST -> "이 노래가 인생의 OST인 이유가 무엇인가요?" to listOf(
            "💫 이 노래를 들었던 그 순간이 특별해서",
            "📖 내게 뜻깊은 교훈을 주어서",
            "😌 행복한 추억을 떠오르게 만들어서",
            "🎙 가수의 음색과 멜로디가 인상적이라서",
            "🖋️ 내 이야기같은 가사에 공감이 되어서",
            "💪🏼 힘들 때 큰 위로와 힘이 돼주어서"
        )
        Category.STORY -> "Q. 이 노래에 담긴 특별한 이야기는 무엇인가요?" to listOf(
            "😍 사랑하는 사람과의 행복했던 시간들",
            "👦🏻 어릴 적 간직한 아련한 추억",
            "👯 소중한 친구들과 즐거웠던 기억",
            "👍🏼 힘든 순간을 이겨내고 극복해낸 경험",
            "⏳ 돌아갈 수 없는 그리운 순간들"
        )
        Category.FREE -> "Q. 이 노래는 OO한 노래다!" to listOf(
            "🧡 사랑하고 싶게 만드는", "😚 즐거움을 주는",
            "🎧 들어도 또 듣고싶은", "👩‍❤️‍👨 연‍‍인과 들으면 딱인",
            "🌃 반짝이는 야경과 어울리는",
            "💊 힘을 주는 비타민 같은"
        )
        else -> "" to emptyList()
    }
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        QnChip(
            question = question1,
            chipList = chipList1
        )
        Spacer(modifier = Modifier.height(56.dp))
        QnChip(
            question = question2,
            description = "(마음껏 골라주세요.)",
            chipList = chipList2
        )
        Spacer(modifier = Modifier.height(56.dp))
        QnChip(
            question = question3,
            description = "(최대 3개까지)",
            chipList = chipList3
        )
        Spacer(modifier = Modifier.height(56.dp))
        Row {
            Text(
                text = "마지막 한 마디",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "(선택)",
                style = MaterialTheme.typography.labelLarge,
                color = grey_03
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                if (it.length <= MAX_ONE_LAST_CONTENTS_LENGTH) {
                    text = it
                    onContentsChange(it)
                }
            },
            placeholder = {
                Text(
                    text = "20자 이내로 작성해주세요.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = grey_03
                )
            },
            shape = MaterialTheme.shapes.roundedField,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QnChip(
    question: String,
    description: String = "",
    chipList: List<String>
) {
    Column {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.labelLarge,
                color = grey_03
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            for (chip in chipList) {
                ChipItem(text = chip)
            }
        }
    }
}

@Composable
fun ChipItem(text: String) {
    Card(
        shape = MaterialTheme.shapes.chip,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 9.dp
            ),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
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

@Preview
@Composable
fun KeywordSelectionBoxPreview() {
    OmosTheme {
        KeywordSelectionBox()
    }
}