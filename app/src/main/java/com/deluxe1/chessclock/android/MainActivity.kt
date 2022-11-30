package com.deluxe1.chessclock.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.deluxe1.chessclock.android.ui.Destinations
import com.deluxe1.chessclock.android.ui.composables.screens.ChessGamePickerScreen
import com.deluxe1.chessclock.android.ui.composables.screens.ChessGameScreen
import com.deluxe1.chessclock.android.ui.composables.screens.GameDetailsScreen
import com.deluxe1.chessclock.android.ui.theme.ChessClockTheme
import com.deluxe1.chessclock.db.ChessGame
import com.deluxe1.chessclock.injectLogger
import com.deluxe1.chessclock.models.ChessGamePickerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

private const val GAME = "GAME"

class MainActivity : ComponentActivity(), KoinComponent {

    private val log: Logger by injectLogger("MainActivity")
    private val chessGamePickerViewModel: ChessGamePickerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            ChessClockTheme {
                NavHost(navController, startDestination = Destinations.GamePicker.destination) {
                    composable(Destinations.GamePicker.destinationWithArguments) { backStackEntry ->
                        ChessGamePickerScreen(
                            viewModel = chessGamePickerViewModel,
                            onNavigateToChessGame = {
                                startGame(navController, it)
                            },
                            onEdit = {
                                backStackEntry.savedStateHandle[GAME] = ChessGameWrapper(it)
                                navController.navigate("${Destinations.GameDetailsScreen.destination}/${it.id}")
                            },
                            onAddNewClicked = { navController.navigate(Destinations.GameDetailsScreen.destination) },
                            log = log
                        )
                    }

                    composable(
                        Destinations.GameScreen.destinationWithArguments,
                        arguments = Destinations.GameScreen.arguments
                    ) {
                        val game = navController.previousBackStackEntry?.savedStateHandle?.get<ChessGameWrapper>(GAME)
                        game?.let {
                            ChessGameScreen(game.getChessGame(), log)
                        }
                    }

                    composable(
                        Destinations.GameDetailsScreen.destinationWithArguments,
                        arguments = Destinations.GameDetailsScreen.arguments
                    ) {
                        val game =
                            navController.previousBackStackEntry?.savedStateHandle?.get<ChessGameWrapper>(
                                key = GAME
                            )
                        GameDetailsScreen(
                            chessGame = game?.getChessGame(),
                            log = log,
                            onAction = { name, durationInMinutes, incrementInSeconds, id, _ ->
                                saveGame(navController, name, durationInMinutes, incrementInSeconds, id)
                            },
                            inputConfig = chessGamePickerViewModel.inputConfig
                        )
                    }

                    composable(
                        Destinations.GameDetailsScreen.destination,
                    ) {
                        GameDetailsScreen(
                            chessGame = null,
                            log = log,
                            onAction = { name, durationInMinutes, incrementInSeconds, id, shouldSave ->
                                if (shouldSave) {
                                   saveGame(navController, name, durationInMinutes, incrementInSeconds, id)
                                }
                                startGame(
                                    navController,
                                    ChessGame(
                                        id = id ?: -1,
                                        name = name,
                                        time = durationInMinutes * 60 * 1000L,
                                        increment = incrementInSeconds.toLong()
                                    )
                                )
                            }, inputConfig = chessGamePickerViewModel.inputConfig)
                    }
                }
            }
        }
    }

    private fun saveGame(navController: NavController, name: String, durationInMinutes: Int, incrementInSeconds: Int, id: Long?) {
        navController.popBackStack()
        chessGamePickerViewModel.insertChessGame(
            name = name,
            durationInMinutes = durationInMinutes,
            incrementInSeconds = incrementInSeconds,
            id = id ?: -1L
        )
    }

    private fun startGame(navController: NavController, game: ChessGame) {
        navController.currentBackStackEntry?.savedStateHandle?.set(GAME, ChessGameWrapper(game))
        navController.navigate("${Destinations.GameScreen.destination}/${game.id}")
    }
}