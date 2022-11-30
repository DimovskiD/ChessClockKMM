package com.deluxe1.chessclock.android.ui.composables.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deluxe1.chessclock.android.R
import com.deluxe1.chessclock.android.ui.composables.organisms.ChessGamePlayerComponent
import com.deluxe1.chessclock.data.GameState
import com.deluxe1.chessclock.models.ChessGameViewModel
import co.touchlab.kermit.Logger
import com.deluxe1.chessclock.db.ChessGame
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChessGameScreen(
    chessGame: ChessGame,
    log: Logger,
    viewModel: ChessGameViewModel = getViewModel(parameters = { parametersOf(chessGame) })
) {

    val chessGameState = viewModel.chessGameState.collectAsState()
    Box {
        Column {
            val player1 = chessGameState.value.playerOne
            val player2 = chessGameState.value.playerTwo
            ChessGamePlayerComponent(
                timeLeft = player2.timeInMillis,
                numberOfMoves = player2.movesMade,
                color = Color.Black,
                onClick = { viewModel.switchPlayer() },
                modifier = Modifier
                    .weight(1f)
                    .rotate(180f)
            )

            ChessGamePlayerComponent(
                timeLeft = player1.timeInMillis,
                numberOfMoves = player1.movesMade,
                color = Color.White,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.switchPlayer() }
            )
        }
        Row(modifier = Modifier.align(Alignment.Center)) {
            if (chessGameState.value.gameState == GameState.FINISHED) {
                RestartButton(viewModel::onRestartClicked)
                Toast.makeText(
                    LocalContext.current,
                    "${viewModel.getWinner()?.playerColor} player wins",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                IconButton(
                    onClick = { viewModel.playPauseClicked() },
                    modifier = Modifier.background(Color.Gray, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (chessGameState.value.gameState != GameState.RESUMED)
                                R.drawable.ic_baseline_play_arrow_24
                            else R.drawable.ic_baseline_pause_24
                        ),
                        contentDescription = null
                    )
                }
                if (chessGameState.value.gameState == GameState.PAUSED) {
                    Spacer(modifier = Modifier.width(8.dp))
                    RestartButton(viewModel::onRestartClicked)
                }
            }
        }
    }
}

@Composable
private fun RestartButton(
    onRestart: () -> Unit
) {
    IconButton(
        onClick = { onRestart() },
        modifier = Modifier.background(Color.Gray, CircleShape)
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.ic_baseline_restart_alt_24
            ),
            contentDescription = null
        )
    }
}