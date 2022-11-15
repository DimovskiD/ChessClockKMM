package co.touchlab.kampkit.android.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import co.touchlab.kampkit.db.ChessGame
import co.touchlab.kampkit.models.ChessGamePickerViewState

@Composable
fun ChessGamePickerScreenContent(
    chessGameState: ChessGamePickerViewState,
    onSuccess: (List<ChessGame>) -> Unit = {},
    onError: (String) -> Unit = {},
    onOpen: (ChessGame) -> Unit = {},
    onCreateNewClicked: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        if (chessGameState.isEmpty) {
            EmptyChessGamePicker(onCreateNewClicked)
        }
        val games = chessGameState.chessGames
        if (games != null) {
            LaunchedEffect(games) {
                onSuccess(games)
            }
            SuccessChessGamePicker(successData = games, onOpen, onCreateNewClicked)
        }
        val error = chessGameState.error
        if (error != null) {
            LaunchedEffect(error) {
                onError(error)
            }
            ErrorChessGamePicker(error)
        }
    }
}