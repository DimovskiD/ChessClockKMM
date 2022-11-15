package co.touchlab.kampkit.usecase

import co.touchlab.kampkit.db.ChessGame
import co.touchlab.kampkit.models.ChessGameRepository
import kotlinx.coroutines.flow.Flow

class GetAllChessGames(private val chessRepository: ChessGameRepository) {

    operator fun invoke() : Flow<List<ChessGame>> = chessRepository.getAllChessGames()

}