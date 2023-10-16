package com.infinity.omos.ui.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun HighlightKeywordInText(
    modifier: Modifier = Modifier,
    text: String,
    keyword: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    keywordColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color.White
) {
    val startIndex = text.indexOf(keyword)
    val endIndex = startIndex + keyword.length

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = textColor)) {
            append(text)
        }
        addStyle(
            style = SpanStyle(color = keywordColor),
            start = startIndex,
            end = endIndex
        )
    }

    Text(
        modifier = modifier,
        text = annotatedText,
        style = style,
        color = textColor
    )
}