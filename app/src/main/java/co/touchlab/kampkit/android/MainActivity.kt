package co.touchlab.kampkit.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kampkit.android.ui.Destinations
import co.touchlab.kampkit.android.ui.composables.screens.ChessGamePickerScreen
import co.touchlab.kampkit.android.ui.composables.screens.ChessGameScreen
import co.touchlab.kampkit.android.ui.theme.KaMPKitTheme
import co.touchlab.kampkit.injectLogger
import co.touchlab.kampkit.models.ChessGamePickerViewModel
import co.touchlab.kampkit.models.ChessGameViewModel
import co.touchlab.kermit.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

    private val log: Logger by injectLogger("MainActivity")
    private val chessGamePickerViewModel: ChessGamePickerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            KaMPKitTheme {
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
