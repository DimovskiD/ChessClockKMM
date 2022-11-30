package com.deluxe1.chessclock.android

import android.os.Parcelable
import com.deluxe1.chessclock.db.ChessGame
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChessGameWrapper(private val name: String, private val duration: Long, private val increment: Long, private val id: Long = -1L) :
    Parcelable {

    constructor(chessGame: ChessGame) : this(chessGame.name, chessGame.time, chessGame.increment, chessGame.id)
    fun getChessGame() = ChessGame(id, name, duration, increment)
}