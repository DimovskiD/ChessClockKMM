package co.touchlab.kampkit.models

import co.touchlab.kampkit.UseCases
import co.touchlab.kampkit.db.ChessGame
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChessGamePickerViewModel(
    private val useCases: UseCases,
    log: Logger
) : ViewModel() {
    private val log = log.withTag("ChessGamesViewModel")

    private val mutableChessGameState: MutableStateFlow<ChessGamePickerViewState> =
        MutableStateFlow(ChessGamePickerViewState(isLoading = true))

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

    fun onCreateNewGameClicked() {
        insertChessGame(ChessGame(-1L, "te2345", 223540, 1))
    }

    private fun insertChessGame(game: ChessGame): Job {
        return viewModelScope.launch {
            useCases.upsertChessGame(game)
        }
    }

    fun deleteChessGame(game: ChessGame) : Job {
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
