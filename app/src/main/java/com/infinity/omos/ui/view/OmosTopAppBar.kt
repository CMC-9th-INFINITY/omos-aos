package com.infinity.omos.ui.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OmosTopAppBar(
    title: String,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    textAlign: TextAlign = TextAlign.Start,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    if (textAlign == TextAlign.Start) {
        TopAppBar(
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = style,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationIcon = navigationIcon,
            actions = actions,
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )
    } else if (textAlign == TextAlign.Center) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = style,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = textAlign
                )
            },
            navigationIcon = navigationIcon,
            actions = actions,
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )
    }
}