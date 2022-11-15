package co.touchlab.kampkit.android.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument

enum class Destinations(
    val destination: String,
    val destinationWithArguments: String,
    val arguments: List<NamedNavArgument>
) {
    GamePicker("game-picker", "game-picker", emptyList()),
    GameScreen(
        "game-screen", "game-screen/{gameId}",
        listOf(navArgument("gameId") { type = androidx.navigation.NavType.LongType })
    )
}