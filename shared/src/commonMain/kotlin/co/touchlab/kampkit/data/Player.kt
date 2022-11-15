package co.touchlab.kampkit.data

data class Player(
    val playerColor: ChessColor,
    var timeInMillis: Long,
    var movesMade: Int,
) {

    fun restart(time: Long) {
        this.timeInMillis = time
        this.movesMade = 0
    }

}

enum class ChessColor {
    BLACK, WHITE
}