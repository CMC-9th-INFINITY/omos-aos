package com.infinity.omos.ui.record.write

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.record.Category
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.gradation
import com.infinity.omos.ui.view.OmosTopAppBar

private const val CARD_HEIGHT = 180

@Composable
fun SelectCategoryScreen(
    viewModel: WriteRecordViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val category = viewModel.category.collectAsState().value

    SelectCategoryScreen(
        category = category,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
        onALineClick = { viewModel.setCategory(Category.A_LINE) },
        onOstClick = { viewModel.setCategory(Category.OST) },
        onStoryClick = { viewModel.setCategory(Category.STORY) },
        onLyricsClick = { viewModel.setCategory(Category.LYRICS) },
        onFreeClick = { viewModel.setCategory(Category.FREE) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCategoryScreen(
    category: Category = Category.A_LINE,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onALineClick: () -> Unit = {},
    onOstClick: () -> Unit = {},
    onStoryClick: () -> Unit = {},
    onLyricsClick: () -> Unit = {},
    onFreeClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            OmosTopAppBar(
                title = stringResource(R.string.select_category),
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
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + 20.dp,
                    start = Dimens.PaddingNormal,
                    end = Dimens.PaddingNormal,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Row {
                CategoryCard(
                    modifier = Modifier.weight(1f),
                    name = stringResource(id = R.string.a_line),
                    description = stringResource(R.string.a_line_description),
                    backgroundDrawable = R.drawable.ic_a_line,
                    isClicked = category == Category.A_LINE,
                    onClick = onALineClick
                )
                Spacer(modifier = Modifier.width(16.dp))
                CategoryCard(
                    modifier = Modifier.weight(1f),
                    name = stringResource(id = R.string.ost),
                    description = stringResource(R.string.ost_description),
                    backgroundDrawable = R.drawable.ic_ost,
                    isClicked = category == Category.OST,
                    onClick = onOstClick
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                CategoryCard(
                    modifier = Modifier.weight(1f),
                    name = stringResource(id = R.string.story),
                    description = stringResource(R.string.story_description),
                    backgroundDrawable = R.drawable.ic_story,
                    isClicked = category == Category.STORY,
                    onClick = onStoryClick
                )
                Spacer(modifier = Modifier.width(16.dp))
                CategoryCard(
                    modifier = Modifier.weight(1f),
                    name = stringResource(id = R.string.lyrics),
                    description = stringResource(R.string.lyrics_description),
                    backgroundDrawable = R.drawable.ic_lyrics,
                    isClicked = category == Category.LYRICS,
                    onClick = onLyricsClick
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CategoryCard(
                modifier = Modifier.fillMaxWidth(),
                name = stringResource(id = R.string.free),
                description = stringResource(R.string.free_description),
                backgroundDrawable = R.drawable.ic_free,
                isClicked = category == Category.FREE,
                onClick = onFreeClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    name: String,
    description: String,
    backgroundDrawable: Int,
    isClicked: Boolean,
    onClick: () -> Unit,
) {
    val border = if (isClicked) {
        BorderStroke(
            width = 2.dp,
            brush = Brush.linearGradient(gradation)
        )
    } else {
        null
    }
    val categoryNameStyle = if (isClicked) {
        MaterialTheme.typography.titleSmall.copy(
            brush = Brush.linearGradient(
                gradation
            )
        )
    } else {
        MaterialTheme.typography.titleSmall
    }

    Card(
        onClick = onClick,
        modifier = modifier.height(CARD_HEIGHT.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = border
    ) {
        Box {
            Image(
                modifier = modifier
                    .fillMaxWidth()
                    .height(CARD_HEIGHT.dp),
                painter = painterResource(id = backgroundDrawable),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(
                    horizontal = Dimens.PaddingNormal,
                    vertical = 20.dp
                )
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = Dimens.PaddingSmall),
                    text = name,
                    style = categoryNameStyle
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelLarge.copy(lineHeight = 22.4.sp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SelectCategoryScreenPreview() {
    OmosTheme {
        SelectCategoryScreen()
    }
}