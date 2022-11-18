package com.deluxe1.chessclock

import com.deluxe1.chessclock.usecase.DeleteChessGame
import com.deluxe1.chessclock.usecase.GetAllChessGames
import com.deluxe1.chessclock.usecase.GetChessGameById
import com.deluxe1.chessclock.usecase.UpsertChessGame

data class UseCases(
    val getAllChessGames: GetAllChessGames,
    val upsertChessGame: UpsertChessGame,
    val deleteChessGame: DeleteChessGame,
    val getChessGameById: GetChessGameById
)