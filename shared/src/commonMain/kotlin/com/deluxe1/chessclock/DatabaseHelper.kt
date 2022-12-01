package com.deluxe1.chessclock

import com.deluxe1.chessclock.db.ChessGame
import com.deluxe1.chessclock.sqldelight.transactionWithContext
import co.touchlab.kermit.Logger
import com.deluxe1.chessclock.db.ChessClockDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DatabaseHelper(
    sqlDriver: SqlDriver,
    private val log: Logger,
    private val backgroundDispatcher: CoroutineDispatcher
) {
    private val dbRef: ChessClockDb = ChessClockDb(sqlDriver)

    fun getAllChessGames(): Flow<List<ChessGame>> =
        dbRef.tableQueries
            .getAllChessGames()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun addChessGames(games: List<ChessGame>) {
        log.d { "Inserting ${games.size} games into database" }
        dbRef.transactionWithContext(backgroundDispatcher) {
            games.forEach { game ->
                dbRef.tableQueries.addChessGame(game.name, game.time, game.increment, if (game.id >=0 ) game.id else null)
            }
        }
    }

    suspend fun deleteChessGames(games: List<ChessGame>) {
        log.d { "Deleting ${games.size} games into database" }
        dbRef.transactionWithContext(backgroundDispatcher) {
            games.forEach { game ->
                dbRef.tableQueries.deleteChessGame(game.id)
            }
        }
    }

    fun getChessGameById(id: Long): Flow<ChessGame> =
        dbRef.tableQueries
            .getChessGameById(id)
            .asFlow()
            .mapToOne()
            .flowOn(backgroundDispatcher)
}
