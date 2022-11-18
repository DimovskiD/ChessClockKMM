package com.deluxe1.chessclock.models

import com.deluxe1.chessclock.DatabaseHelper
import com.deluxe1.chessclock.db.ChessGame

class ChessGameRepository(
    private val dbHelper: DatabaseHelper
) {

    fun getChessGame(chessGameId: Long) = dbHelper.getChessGameById(chessGameId)

    fun getAllChessGames() = dbHelper.getAllChessGames()

    suspend fun addChessGameDetails(chessGameEntity: ChessGame) = dbHelper.addChessGames(listOf(chessGameEntity))

    suspend fun deleteChessGame(chessGame: ChessGame) = dbHelper.deleteChessGames(listOf(chessGame))
}