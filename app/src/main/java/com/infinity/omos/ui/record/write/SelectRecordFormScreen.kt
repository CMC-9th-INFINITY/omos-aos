package com.infinity.omos.ui.record.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.record.Category
import com.infinity.omos.ui.record.RecordForm
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.view.BackIcon
import com.infinity.omos.ui.view.OmosTopAppBar

@Composable
fun SelectRecordFormScreen(
    viewModel: WriteRecordViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: (Category, RecordForm) -> Unit
) {
    val category = viewModel.category.collectAsState().value
    val recordForm = viewModel.recordForm.collectAsState().value

    SelectRecordFormScreen(
        recordForm = recordForm,
        onBackClick = onBackClick,
        onNextClick = { onNextClick(category, recordForm) },
        onWriteClick = { viewModel.setRecordForm(RecordForm.WRITE) },
        onKeywordClick = { viewModel.setRecordForm(RecordForm.KEYWORD) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRecordFormScreen(
    recordForm: RecordForm = RecordForm.WRITE,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onWriteClick: () -> Unit = {},
    onKeywordClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            OmosTopAppBar(
                title = stringResource(R.string.select_record_form),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                navigationIcon = { BackIcon(onClick = onBackClick) },
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
                    top = paddingValues.calculateTopPadding() + 40.dp,
                    start = Dimens.PaddingNormal,
                    end = Dimens.PaddingNormal,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            CategoryCard(
                name = stringResource(R.string.write_record),
                description = stringResource(R.string.write_record_description),
                backgroundDrawable = R.drawable.img_write_record,
                isClicked = recordForm == RecordForm.WRITE,
                onClick = onWriteClick
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingNormal))
            CategoryCard(
                name = stringResource(R.string.select_keyword),
                description = stringResource(R.string.select_keyword_description),
                backgroundDrawable = R.drawable.img_select_keyword,
                isClicked = recordForm == RecordForm.KEYWORD,
                onClick = onKeywordClick
            )
        }
    }
}

@Preview
@Composable
fun SelectRecordFormScreenPreview() {
    OmosTheme {
        SelectRecordFormScreen()
    }
}