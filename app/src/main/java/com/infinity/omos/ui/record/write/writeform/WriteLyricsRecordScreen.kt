package com.infinity.omos.ui.record.write.writeform

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.record.write.MusicTopBar
import com.infinity.omos.ui.record.write.WriteRecordViewModel
import com.infinity.omos.ui.view.OmosTopAppBar

@Composable
fun WriteLyricsRecordScreen(
    viewModel: WriteRecordViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val music = viewModel.music.collectAsState().value

    WriteLyricsRecordScreen(
        music = music,
        onBackClick = onBackClick,
        onNextClick = onNextClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteLyricsRecordScreen(
    music: MusicModel,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            OmosTopAppBar(
                title = stringResource(R.string.record_write),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    Text(
                        modifier = Modifier
                            .padding(Dimens.PaddingNormal)
                            .clickable { onNextClick() },
                        text = stringResource(id = R.string.next),
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
        }
    }
}