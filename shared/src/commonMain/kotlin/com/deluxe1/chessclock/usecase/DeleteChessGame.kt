package com.deluxe1.chessclock.usecase

import com.deluxe1.chessclock.db.ChessGame
import com.deluxe1.chessclock.models.ChessGameRepository

class DeleteChessGame(private val chessRepository: ChessGameRepository) {

    suspend operator fun invoke(chessGame : ChessGame) = chessRepository.deleteChessGame(chessGame)

}