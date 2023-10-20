package com.infinity.omos.ui.record.write.writeform

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.record.write.MusicTopBar
import com.infinity.omos.ui.record.write.TextCount
import com.infinity.omos.ui.record.write.WriteRecordViewModel
import com.infinity.omos.ui.theme.white
import com.infinity.omos.ui.view.OmosTopAppBar

private const val MAX_TEXT_LENGTH = 250

@Composable
fun WriteLyricsRecordScreen(
    viewModel: WriteRecordViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val music = viewModel.music.collectAsState().value
    val contents = viewModel.contents.collectAsState().value

    WriteLyricsRecordScreen(
        music = music,
        contents = contents,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
        onTextChange = { viewModel.setContents(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteLyricsRecordScreen(
    music: MusicModel,
    contents: String,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onTextChange: (String) -> Unit = {}
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
            var text by remember { mutableStateOf(contents) }

            Spacer(modifier = Modifier.height(12.dp))
            MusicTopBar(music = music)
            Spacer(modifier = Modifier.height(20.dp))
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = Dimens.PaddingNormal)
                    .background(Color.Transparent)
                ,
                value = text,
                onValueChange = {
                    if (it.length <= MAX_TEXT_LENGTH) {
                        text = it
                        onTextChange(it)
                    }
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = white,
                    fontWeight = W400,
                    lineHeight = 25.6.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
            )
            Divider(
                modifier = Modifier.padding(20.dp),
                thickness = 1.dp
            )
            TextCount(
                modifier = Modifier.padding(horizontal = Dimens.PaddingNormal),
                name = stringResource(R.string.text_count),
                count = text.length,
                maxLength = MAX_TEXT_LENGTH
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}