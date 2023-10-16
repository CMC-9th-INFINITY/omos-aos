package com.infinity.omos.ui.music.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.view.OmosTopAppBar

@Composable
fun MusicSearchScreen(
    onBackClick: () -> Unit,
    viewModel: MusicSearchViewModel = hiltViewModel()
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicSearchScreen(
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            OmosTopAppBar(
                title = stringResource(id = R.string.search_music),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
        }

        // 인기 검색어, 음악 타이틀, Pager
    }
}

@Preview
@Composable
fun MusicSearchScreenPreview() {
    OmosTheme {
        MusicSearchScreen()
    }
}