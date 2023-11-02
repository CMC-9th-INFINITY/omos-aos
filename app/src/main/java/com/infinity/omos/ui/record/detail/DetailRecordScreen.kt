package com.infinity.omos.ui.record.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.data.record.RecordCategory
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.record.MusicTopBar
import com.infinity.omos.ui.record.RecordTitleBox
import com.infinity.omos.ui.theme.alineContents
import com.infinity.omos.ui.theme.basicContents
import com.infinity.omos.ui.theme.black_02
import com.infinity.omos.ui.theme.grey_01
import com.infinity.omos.ui.theme.lyricsContents
import com.infinity.omos.ui.view.BackIcon
import com.infinity.omos.ui.view.OmosTopAppBar

@Composable
fun DetailRecordScreen(
    viewModel: DetailRecordViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val detailRecordUiState = viewModel.detailRecordUiState.collectAsState().value

    DetailRecordScreen(
        detailRecordUiState = detailRecordUiState,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRecordScreen(
    detailRecordUiState: DetailRecordUiState,
    onBackClick: () -> Unit = {},
) {
    var category by remember { mutableStateOf(RecordCategory.A_LINE) }
    if (detailRecordUiState is DetailRecordUiState.Success) {
        with(detailRecordUiState.detailRecord) {
            category = this.category
        }
    }

    Scaffold(
        topBar = {
            OmosTopAppBar(
                title = category.str,
                navigationIcon = { BackIcon(onClick = onBackClick) },
                actions = {
                    Icon(
                        modifier = Modifier.clickable {
                            // 인스타 공유
                        },
                        painter = painterResource(id = R.drawable.ic_insta),
                        contentDescription = "인스타그램"
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Icon(
                        modifier = Modifier.clickable {
                            // 레코드 수정 및 삭제
                        },
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = "더보기"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            )
        }
    ) { paddingValues ->
        if (detailRecordUiState is DetailRecordUiState.Success) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .background(color = black_02)
            ) {
                val lazyListState = rememberLazyListState()

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .background(color = MaterialTheme.colorScheme.background)
                        )
                        MusicTopBar(music = detailRecordUiState.detailRecord.music)
                        RecordTitleBox(
                            title = detailRecordUiState.detailRecord.recordTitle,
                            imageUrl = detailRecordUiState.detailRecord.recordImageUrl,
                            date = detailRecordUiState.detailRecord.date,
                            isMine = detailRecordUiState.detailRecord.isMine,
                            isPublic = detailRecordUiState.detailRecord.isPublic
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    when (category) {
                        RecordCategory.A_LINE -> item {
                            ALineContents(
                                contents = detailRecordUiState.detailRecord.recordContents
                            )
                        }

                        RecordCategory.LYRICS -> item {
                            LyricsContents(
                                contents = detailRecordUiState.detailRecord.recordContents
                            )
                        }

                        else -> item {
                            BasicCategoryContents(
                                contents = detailRecordUiState.detailRecord.recordContents,
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(
                            modifier = Modifier.padding(
                                bottom = 20.dp,
                                start = 20.dp,
                                end = 20.dp
                            ),
                            thickness = 1.dp
                        )
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = detailRecordUiState.detailRecord.nickname,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            LikeButton(
                                isLiked = detailRecordUiState.detailRecord.isLiked,
                                sympathyCount = detailRecordUiState.detailRecord.sympathyCount
                            )
                            Spacer(modifier = Modifier.width(24.dp))
                            ScrapButton(
                                isScrapped = detailRecordUiState.detailRecord.isScrapped,
                                scrapCount = detailRecordUiState.detailRecord.scrapCount
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@Composable
private fun ALineContents(
    modifier: Modifier = Modifier,
    contents: String
) {
    Text(
        modifier = modifier.padding(48.dp),
        text = contents,
        style = MaterialTheme.typography.alineContents
    )
}

@Composable
private fun LyricsContents(
    modifier: Modifier = Modifier,
    contents: String
) {
    val lyricsList = contents.split("\n")
    Column {
        lyricsList.forEachIndexed { index, row ->
            if (index % 2 == 0) {
                Text(
                    modifier = modifier,
                    text = row,
                    style = MaterialTheme.typography.lyricsContents
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Text(
                    modifier = modifier,
                    text = row,
                    style = MaterialTheme.typography.basicContents
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun BasicCategoryContents(
    modifier: Modifier = Modifier,
    contents: String
) {
    Text(
        modifier = modifier.padding(horizontal = Dimens.PaddingNormal),
        text = contents,
        style = MaterialTheme.typography.basicContents
    )
}

@Composable
private fun LikeButton(
    isLiked: Boolean,
    sympathyCount: Int
) {
    var clicked by remember { mutableStateOf(isLiked) }
    var cnt by remember { mutableIntStateOf(sympathyCount) }
    Column {
        Icon(
            modifier = Modifier.clickable {
                clicked = !clicked
                if (clicked) {
                    cnt += 1
                } else {
                    cnt -= 1
                }
            },
            painter = painterResource(
                id = if (clicked) {
                    R.drawable.ic_checked_heart
                } else {
                    R.drawable.ic_unchecked_heart
                }
            ),
            contentDescription = "공감 버튼",
            tint = if (clicked) {
                MaterialTheme.colorScheme.primary
            } else {
                grey_01
            }
        )
        Text(
            text = formatCountNumber(cnt),
            style = MaterialTheme.typography.labelMedium,
            color = if (clicked) {
                MaterialTheme.colorScheme.primary
            } else {
                grey_01
            }
        )
    }
}

@Composable
private fun ScrapButton(
    isScrapped: Boolean,
    scrapCount: Int
) {
    var clicked by remember { mutableStateOf(isScrapped) }
    var cnt by remember { mutableIntStateOf(scrapCount) }
    Column {
        Icon(
            modifier = Modifier.clickable {
                clicked = !clicked
                if (clicked) {
                    cnt += 1
                } else {
                    cnt -= 1
                }
            },
            painter = painterResource(
                id = if (clicked) {
                    R.drawable.ic_checked_star
                } else {
                    R.drawable.ic_unchecked_star
                }
            ),
            contentDescription = "스크랩 버튼",
            tint = if (clicked) {
                MaterialTheme.colorScheme.primary
            } else {
                grey_01
            }
        )
        Text(
            text = formatCountNumber(cnt),
            style = MaterialTheme.typography.labelMedium,
            color = if (clicked) {
                MaterialTheme.colorScheme.primary
            } else {
                grey_01
            }
        )
    }
}

private fun formatCountNumber(number: Int): String {
    return when {
        number < 0 -> "000"
        number > 999 -> "999+"
        else -> String.format("%03d", number)
    }
}