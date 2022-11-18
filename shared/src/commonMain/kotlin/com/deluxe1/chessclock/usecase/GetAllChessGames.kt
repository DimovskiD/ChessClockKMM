package com.deluxe1.chessclock.usecase

import com.deluxe1.chessclock.db.ChessGame
import com.deluxe1.chessclock.models.ChessGameRepository
import kotlinx.coroutines.flow.Flow

class GetAllChessGames(private val chessRepository: ChessGameRepository) {

    operator fun invoke() : Flow<List<ChessGame>> = chessRepository.getAllChessGames()

}