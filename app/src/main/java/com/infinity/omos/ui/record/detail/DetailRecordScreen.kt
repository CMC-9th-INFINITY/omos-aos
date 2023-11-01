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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.data.record.RecordCategory
import com.infinity.omos.factory.createEmptyMusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.record.MusicTopBar
import com.infinity.omos.ui.record.RecordTitleBox
import com.infinity.omos.ui.record.write.ALineBox
import com.infinity.omos.ui.record.write.LyricsBox
import com.infinity.omos.ui.theme.black_02
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
                    Icon(
                        modifier = Modifier.clickable {
                            // 레코드 수정 및 삭제
                        },
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = "더보기"
                    )
                }
            )
        }
    ) { paddingValues ->
        if (detailRecordUiState is DetailRecordUiState.Success) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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

                    /*when (category) {
                        RecordCategory.A_LINE -> item {
                            ALineBox(
                                modifier = Modifier.fillParentMaxSize(),
                                contents = contents,
                                onContentsChange = {
                                    contents = it
                                    onContentsChange(it)
                                }
                            )
                        }

                        RecordCategory.LYRICS -> itemsIndexed(
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

                        else -> {}
                    }*/

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
                        Row {

                        }
                        Spacer(modifier = Modifier.height(Dimens.PaddingNormal))
                    }
                }
            }
        }
    }
}