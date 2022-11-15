package co.touchlab.kampkit

import co.touchlab.kampkit.usecase.DeleteChessGame
import co.touchlab.kampkit.usecase.GetAllChessGames
import co.touchlab.kampkit.usecase.GetChessGameById
import co.touchlab.kampkit.usecase.UpsertChessGame

data class UseCases(
    val getAllChessGames: GetAllChessGames,
    val upsertChessGame: UpsertChessGame,
    val deleteChessGame: DeleteChessGame,
    val getChessGameById: GetChessGameById
)