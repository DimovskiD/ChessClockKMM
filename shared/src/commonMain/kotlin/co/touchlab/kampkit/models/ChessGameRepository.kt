package co.touchlab.kampkit.models

import co.touchlab.kampkit.DatabaseHelper
import co.touchlab.kampkit.db.ChessGame

class ChessGameRepository(
    private val dbHelper: DatabaseHelper
) {

    fun getChessGame(chessGameId: Long) = dbHelper.getChessGameById(chessGameId)

    fun getAllChessGames() = dbHelper.getAllChessGames()

    suspend fun addChessGameDetails(chessGameEntity: ChessGame) = dbHelper.addChessGames(listOf(chessGameEntity))

    suspend fun deleteChessGame(chessGame: ChessGame) = dbHelper.deleteChessGames(listOf(chessGame))
}