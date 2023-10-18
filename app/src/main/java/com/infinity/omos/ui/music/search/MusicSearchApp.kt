package com.infinity.omos.ui.music.search

import androidx.compose.runtime.Composable
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

@Composable
fun MusicSearchApp() {
    val navController = rememberNavController()
    MusicSearchNavHost(
        navController = navController
    )
}

@Composable
fun MusicSearchNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "musicSearch") {
        composable("musicSearch") {
            MusicSearchScreen(
                onBackClick = { navController.navigateUp() },
                onMusicClick = {},
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
                onMusicClick = {}
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
    }
}

fun NavController.navigateAlbumScreen(album: AlbumModel) {
    val destination =
        "album/${album.albumId}/${album.albumTitle}/${album.artists}/${album.releaseDate}?albumImageUrl=${album.albumImageUrl}"
    navigate(destination)
}