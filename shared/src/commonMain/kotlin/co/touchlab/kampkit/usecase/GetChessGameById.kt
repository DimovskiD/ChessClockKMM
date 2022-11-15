package co.touchlab.kampkit.usecase


import co.touchlab.kampkit.db.ChessGame
import co.touchlab.kampkit.models.ChessGameRepository
import kotlinx.coroutines.flow.Flow

class GetChessGameById(private val chessRepository: ChessGameRepository) {

    operator fun invoke(id: Long) : Flow<ChessGame> = chessRepository.getChessGame(id)

}