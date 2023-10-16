package com.infinity.omos.ui.music.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.data.music.TopSearchedModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.field
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
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = Dimens.PaddingNormal)
        ) {
            SearchBar(
                modifier = Modifier.padding(top = Dimens.PaddingSmall),
                onSearch = {},
                onKeywordChange = {}
            )

            TopSearchedList(modifier = Modifier.padding(top = 28.dp))
        }

        // 인기 검색어, 음악 타이틀, Pager
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier,
    onSearch: (String) -> Unit,
    onKeywordChange: (String) -> Unit
) {
    var keyword by remember { mutableStateOf("") }

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = keyword,
        onValueChange = {
            keyword = it
            onKeywordChange(it)
        },
        placeholder = { Text(text = stringResource(id = R.string.please_search_music)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.search)
            )
        },
        trailingIcon = {
            if (keyword.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable { keyword = "" },
                    painter = painterResource(id = R.drawable.ic_clear),
                    contentDescription = stringResource(R.string.clear)
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions { onSearch(keyword) },
        shape = MaterialTheme.shapes.field,
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun TopSearchedList(
    modifier: Modifier
) {
    val lazyListState = rememberLazyListState()

    // 인기 검색어 API가 없어 임의로 만든 데이터
    val topSearchedList = listOf(
        TopSearchedModel("1", "인피니티"),
        TopSearchedModel("2", "OMOS"),
        TopSearchedModel("3", "유니"),
        TopSearchedModel("4", "루시"),
        TopSearchedModel("5", "브리"),
        TopSearchedModel("6", "숨"),
        TopSearchedModel("7", "클로이"),
        TopSearchedModel("8", "재르민")
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState
    ) {
        item {
            Row(modifier = Modifier.padding(bottom = 18.dp)) {
                Text(
                    text = stringResource(id = R.string.top_searched),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = stringResource(id = R.string.standard_yesterday),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        items(
            items = topSearchedList,
            key = { it.keyword }
        ) {
            TopSearchedListItem(
                topSearched = it
            )
        }
    }
}

@Composable
fun TopSearchedListItem(
    topSearched: TopSearchedModel
) {
    Row(modifier = Modifier.padding(vertical = 14.dp)) {
        Text(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .align(Alignment.CenterVertically),
            text = topSearched.ranking,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp)
                .align(Alignment.CenterVertically),
            text = topSearched.keyword,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun MusicSearchScreenPreview() {
    OmosTheme {
        MusicSearchScreen()
    }
}