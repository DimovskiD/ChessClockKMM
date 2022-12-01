package com.deluxe1.chessclock.models

import com.deluxe1.chessclock.UseCases
import com.deluxe1.chessclock.db.ChessGame
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChessGamePickerViewModel(
    private val useCases: UseCases,
    log: Logger
) : ViewModel() {
    private val log = log.withTag("ChessGamesViewModel")

    private val mutableChessGameState: MutableStateFlow<ChessGamePickerViewState> =
        MutableStateFlow(ChessGamePickerViewState(isLoading = true))

    val inputConfig = InputConfig(30, 3, 3)
    val chessGameState: StateFlow<ChessGamePickerViewState> = mutableChessGameState

    init {
        observeChessGames()
    }

    override fun onCleared() {
        log.v("Clearing BreedViewModel")
    }

    private fun observeChessGames() {
        viewModelScope.launch {
            useCases.getAllChessGames().collect { games ->
                mutableChessGameState.update {
                    ChessGamePickerViewState(
                        isLoading = false,
                        chessGames = games.takeIf { it.isNotEmpty() },
                        error = null,
                        isEmpty = games.isEmpty()
                    )
                }
            }
        }
    }

    fun insertChessGame(
        name: String,
        durationInMinutes: Int,
        incrementInSeconds: Int,
        id: Long
    ) {
        val duration = durationInMinutes * 60 * 1000L
        val increment = incrementInSeconds.toLong()
        insertChessGame(ChessGame(name = name, time = duration, increment = increment, id = id))
    }

    fun isValid(name: String, durationInMinutes: Int, incrementInSeconds: Int) =
        name.isNotEmpty() && durationInMinutes > 0 && incrementInSeconds >= 0

    private fun insertChessGame(game: ChessGame): Job {
        return viewModelScope.launch {
            useCases.upsertChessGame(game)
        }
    }

    fun deleteChessGame(game: ChessGame): Job {
        log.i("DELETE GAME $game")
        return viewModelScope.launch {
            useCases.deleteChessGame(game)
        }
    }
}

data class ChessGamePickerViewState(
    val chessGames: List<ChessGame>? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)
