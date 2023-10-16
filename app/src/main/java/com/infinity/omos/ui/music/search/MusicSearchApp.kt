package com.infinity.omos.ui.music.search

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.infinity.omos.ui.music.search.album.AlbumScreen

@Composable
fun MusicSearchApp() {
    val navController = rememberNavController()
    MusicSearchNavHost(
        navController = navController
    )
}

@Composable
fun MusicSearchNavHost(
    navController: NavHostController,
    viewModel: MusicSearchViewModel = hiltViewModel()
) {
    NavHost(navController = navController, startDestination = "pager") {
        composable("pager") {
            MusicSearchPagerScreen(
                onAlbumClick = {
                    val destination = "album/${it.albumId}/${it.albumTitle}/${it.artists}/${it.releaseDate}?albumImageUrl=${it.albumImageUrl}"
                    navController.navigate(destination)
                },
                onArtistClick = {

                },
                viewModel = viewModel
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
            AlbumScreen()
        }
    }
}