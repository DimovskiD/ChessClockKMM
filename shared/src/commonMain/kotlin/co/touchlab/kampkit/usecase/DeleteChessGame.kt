package co.touchlab.kampkit.usecase

import co.touchlab.kampkit.db.ChessGame
import co.touchlab.kampkit.models.ChessGameRepository

class DeleteChessGame(private val chessRepository: ChessGameRepository) {

    suspend operator fun invoke(chessGame : ChessGame) = chessRepository.deleteChessGame(chessGame)

}