package com.deluxe1.chessclock.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.deluxe1.chessclock.android.ui.Destinations
import com.deluxe1.chessclock.android.ui.composables.screens.ChessGamePickerScreen
import com.deluxe1.chessclock.android.ui.composables.screens.ChessGameScreen
import com.deluxe1.chessclock.android.ui.theme.ChessClockTheme
import com.deluxe1.chessclock.injectLogger
import com.deluxe1.chessclock.models.ChessGamePickerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

    private val log: Logger by injectLogger("MainActivity")
    private val chessGamePickerViewModel: ChessGamePickerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            ChessClockTheme {
                NavHost(navController, startDestination = Destinations.GamePicker.destination) {
                    composable(Destinations.GamePicker.destinationWithArguments) {
                        ChessGamePickerScreen(
                            chessGamePickerViewModel,
                            { navController.navigate("${Destinations.GameScreen.destination}/${it.id}") },
                            log
                        )
                    }
                    composable(
                        Destinations.GameScreen.destinationWithArguments,
                        arguments = Destinations.GameScreen.arguments
                    ) {
                        ChessGameScreen(it.arguments?.getLong(Destinations.GameScreen.arguments[0].name), log)
                    }
                }
            }
        }
    }
}
