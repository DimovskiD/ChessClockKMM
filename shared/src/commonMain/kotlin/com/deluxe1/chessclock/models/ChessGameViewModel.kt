package com.deluxe1.chessclock.models

import com.deluxe1.chessclock.KMMTimer
import com.deluxe1.chessclock.UseCases
import com.deluxe1.chessclock.data.ChessColor
import com.deluxe1.chessclock.data.GameState
import com.deluxe1.chessclock.data.Player
import com.deluxe1.chessclock.db.ChessGame
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChessGameViewModel(
    private val gameId: Long,
    private val useCases: UseCases,
    log: Logger
) : ViewModel() {
    private val log = log.withTag("ChessGamesViewModel")
    private val timer: KMMTimer = KMMTimer("ChessTimer", 100, 0) {
        log.i("TIMER WORKS")
        if (mutableChessGameState.value.activePlayer != null && mutableChessGameState.value.gameState == GameState.RESUMED) {
            mutableChessGameState.update {
                if (it.activePlayer != null) {
                    val remainingTime = it.activePlayer.timeInMillis - 100
                    if (remainingTime <= 0) {
                        return@update it.copy(gameState = GameState.FINISHED)
                    }
                    val player = Player(
                        it.activePlayer.playerColor,
                        remainingTime,
                        it.activePlayer.movesMade
                    )
                    if (it.activePlayer == it.playerOne) {
                        return@update it.copy(playerOne = player, activePlayer = player)
                    } else return@update it.copy(playerTwo = player, activePlayer = player)
                }
                it
            }
        }
    }

    private val mutableChessGameState: MutableStateFlow<ChessGameViewState> =
        MutableStateFlow(
            ChessGameViewState(
                ChessGame(-1L, "", 0, 0),
            )
        )

    val chessGameState: StateFlow<ChessGameViewState> = mutableChessGameState

    init {
        viewModelScope.launch {
            useCases.getChessGameById(gameId).collect { game ->
                mutableChessGameState.value = ChessGameViewState(game)
            }
            chessGameState.collect {
                if (it.activePlayer != null && it.activePlayer.timeInMillis <= 0) timer.cancel()
            }
        }
    }

    override fun onCleared() {
        timer.cancel()
    }

    fun playPauseClicked() {
        if (chessGameState.value.gameState == GameState.PAUSED || chessGameState.value.gameState == GameState.NOT_STARTED) resumeGame()
        else pauseGame()
    }

    fun onRestartClicked() {
        mutableChessGameState.update {
            ChessGameViewState(it.chessGame)
        }
    }

    private fun pauseGame() {
        mutableChessGameState.update {
            it.copy(gameState = GameState.PAUSED)
        }
        timer.cancel()
    }

    private fun resumeGame() {
        mutableChessGameState.update {
            it.copy(gameState = GameState.RESUMED, activePlayer = it.activePlayer ?: it.playerOne)
        }
        if (mutableChessGameState.value.activePlayer != null) {
            timer.start()
        }
    }

    fun switchPlayer() {
        if (mutableChessGameState.value.gameState != GameState.RESUMED) return
        mutableChessGameState.update { state ->
            state.activePlayer?.let {
                it.movesMade++
                if (state.chessGame.increment > 0) it.timeInMillis += state.chessGame.increment * 1000
            }
            state.copy(activePlayer = if (state.activePlayer?.playerColor == state.playerOne.playerColor) state.playerTwo else state.playerOne)
        }
    }

    fun getWinner(): Player? = with(chessGameState.value) {
        return@with if (this.gameState != GameState.FINISHED) null
        else if (activePlayer == playerOne) playerTwo else playerOne
    }
}

data class ChessGameViewState(
    val chessGame: ChessGame,
    val playerOne: Player = Player(ChessColor.WHITE, chessGame.time, 0),
    val playerTwo: Player = Player(ChessColor.BLACK, chessGame.time, 0),
    val activePlayer: Player? = null,
    val gameState: GameState = GameState.NOT_STARTED
)
