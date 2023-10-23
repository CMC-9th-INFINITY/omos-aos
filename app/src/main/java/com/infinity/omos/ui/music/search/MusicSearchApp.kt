package com.infinity.omos.ui.music.search

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.infinity.omos.data.music.album.AlbumModel
import com.infinity.omos.ui.music.search.album.AlbumScreen
import com.infinity.omos.ui.music.search.artist.ArtistScreen
import com.infinity.omos.ui.record.RecordForm
import com.infinity.omos.ui.record.write.SelectCategoryScreen
import com.infinity.omos.ui.record.write.SelectRecordFormScreen
import com.infinity.omos.ui.record.write.WriteRecordViewModel
import com.infinity.omos.ui.record.write.writeform.WriteLyricsScreen
import com.infinity.omos.ui.record.write.writeform.WriteRecordScreen

@Composable
fun MusicSearchApp() {
    val navController = rememberNavController()
    MusicSearchNavHost(
        navController = navController
    )
}

@Composable
fun MusicSearchNavHost(
    writeRecordViewModel: WriteRecordViewModel = hiltViewModel(),
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "musicSearch") {
        composable("musicSearch") {
            MusicSearchScreen(
                onBackClick = { navController.navigateUp() },
                onMusicClick = { navController.navigateSelectCategoryScreen(it) },
                onAlbumClick = { navController.navigateAlbumScreen(it) },
                onArtistClick = {
                    val destination =
                        "artist/${it.artistId}/${it.artistName}"
                    navController.navigate(destination)
                }
            )
        }
        composable(
            "album/{albumId}/{albumTitle}/{artists}/{releaseDate}?albumImageUrl={albumImageUrl}",
            arguments = listOf(
                navArgument("albumId") { type = NavType.StringType },
                navArgument("albumTitle") { type = NavType.StringType },
                navArgument("albumImageUrl") { type = NavType.StringType },
                navArgument("artists") { type = NavType.StringType },
                navArgument("releaseDate") { type = NavType.StringType },
            )
        ) {
            AlbumScreen(
                onBackClick = { navController.navigateUp() },
                onMusicClick = { navController.navigateSelectCategoryScreen(it) }
            )
        }
        composable(
            "artist/{artistId}/{artistName}",
            arguments = listOf(
                navArgument("artistId") { type = NavType.StringType },
                navArgument("artistName") { type = NavType.StringType }
            )
        ) {
            ArtistScreen(
                onBackClick = { navController.navigateUp() },
                onAlbumClick = { navController.navigateAlbumScreen(it) }
            )
        }
        composable(
            "selectCategory/{musicId}",
            arguments = listOf(
                navArgument("musicId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val musicId = backStackEntry.arguments?.getString("musicId") ?: ""

            SelectCategoryScreen(
                viewModel = writeRecordViewModel,
                musicId = musicId,
                onBackClick = { navController.navigateUp() },
                onNextClick = {
                    val direction = "selectRecordForm"
                    navController.navigate(direction)
                }
            )
        }
        composable(
            "selectRecordForm",
            arguments = listOf()
        ) {
            SelectRecordFormScreen(
                viewModel = writeRecordViewModel,
                onBackClick = { navController.navigateUp() },
                onNextClick = { recordForm ->
                    when(recordForm) {
                        RecordForm.WRITE -> {
                            val direction = "writeLyrics"
                            navController.navigate(direction)
                        }
                        RecordForm.KEYWORD -> {

                        }
                    }
                }
            )
        }
        composable(
            "writeLyrics",
            arguments = listOf()
        ) {
            WriteLyricsScreen(
                viewModel = writeRecordViewModel,
                onBackClick = { navController.navigateUp() },
                onNextClick = {
                    val direction = "writeRecord"
                    navController.navigate(direction)
                }
            )
        }
        composable(
            "writeRecord"
        ) {
            WriteRecordScreen(
                viewModel = writeRecordViewModel,
                onBackClick = { navController.navigateUp() },
                onCompleteClick = {}
            )
        }
    }
}

fun NavController.navigateSelectCategoryScreen(musicId: String) {
    val destination = "selectCategory/${musicId}"
    navigate(destination)
}

fun NavController.navigateAlbumScreen(album: AlbumModel) {
    val destination =
        "album/${album.albumId}/${album.albumTitle}/${album.artists}/${album.releaseDate}?albumImageUrl=${album.albumImageUrl}"
    navigate(destination)
}