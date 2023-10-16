package com.infinity.omos.ui.music.search.pager

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.infinity.omos.R
import com.infinity.omos.ui.Dimens

@Composable
fun PageHeader(title: String, isVisibleMore: Boolean, onMoreClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 29.dp)
            .padding(bottom = Dimens.PaddingNormal)
            .padding(horizontal = Dimens.PaddingNormal),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isVisibleMore) {
            ClickableText(
                modifier = Modifier.align(Alignment.Bottom),
                text = AnnotatedString(stringResource(id = R.string.more)),
                style = MaterialTheme.typography.labelMedium
            ) {
                onMoreClick()
            }
        }
    }
}