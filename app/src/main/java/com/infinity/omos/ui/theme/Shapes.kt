package com.infinity.omos.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes.field: CornerBasedShape
    get() = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp,
        bottomStart = 8.dp,
        bottomEnd = 8.dp
    )

val Shapes.roundedField: CornerBasedShape
    get() = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )

val Shapes.chip: CornerBasedShape
    get() = RoundedCornerShape(
        topStart = 30.dp,
        topEnd = 30.dp,
        bottomStart = 30.dp,
        bottomEnd = 30.dp
    )