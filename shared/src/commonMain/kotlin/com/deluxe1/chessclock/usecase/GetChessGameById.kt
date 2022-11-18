package com.deluxe1.chessclock.usecase


import com.deluxe1.chessclock.db.ChessGame
import com.deluxe1.chessclock.models.ChessGameRepository
import kotlinx.coroutines.flow.Flow

class GetChessGameById(private val chessRepository: ChessGameRepository) {

    operator fun invoke(id: Long) : Flow<ChessGame> = chessRepository.getChessGame(id)

}