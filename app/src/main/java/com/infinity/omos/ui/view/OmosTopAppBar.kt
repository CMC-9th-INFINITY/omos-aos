package com.infinity.omos.ui.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    modifier: Modifier = Modifier,
    title: String = "",
    style: TextStyle = MaterialTheme.typography.titleLarge,
    textAlign: TextAlign = TextAlign.Start,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    if (textAlign == TextAlign.Start) {
        TopAppBar(
            modifier = modifier,
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
            modifier = modifier,
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

@Composable
fun BackIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Go back",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}