package com.infinity.omos.ui.record.write

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infinity.omos.R
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.theme.grey_01
import com.infinity.omos.ui.theme.grey_03
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MusicTopBar(
    music: MusicModel
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            GlideImage(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = grey_03),
                imageModel = { music.albumImageUrl },
                failure = {
                    Image(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(id = R.drawable.ic_music),
                        contentDescription = stringResource(id = R.string.music_icon)
                    )
                }
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
                    .weight(1f),
            ) {
                Text(
                    text = music.musicTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = music.artistsAndAlbumTitle,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Image(
                modifier = Modifier
                    .padding(horizontal = Dimens.PaddingNormal)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_spotify),
                contentDescription = stringResource(id = R.string.spotify_icon)
            )
        }
    }
}

@Composable
fun TextCount(
    modifier: Modifier,
    name: String,
    count: Int,
    maxLength: Int
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            fontSize = 14.sp,
            color = grey_03
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${count}/${maxLength}자",
            fontSize = 14.sp,
            color = grey_01
        )
    }
}