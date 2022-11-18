package com.deluxe1.chessclock.usecase

import com.deluxe1.chessclock.db.ChessGame
import com.deluxe1.chessclock.models.ChessGameRepository

class UpsertChessGame(private val chessRepository: ChessGameRepository) {

    suspend operator fun invoke(chessGame: ChessGame) = chessRepository.addChessGameDetails(chessGame)

}