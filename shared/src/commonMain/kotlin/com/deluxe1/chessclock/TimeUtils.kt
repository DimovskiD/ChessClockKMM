package com.deluxe1.chessclock

fun Long.formatTime(showMillis: Boolean): String {
    val seconds = ((this / 1000).toInt() % 60).doubleDigit()
    val minutes = ((this / (1000 * 60) % 60).toInt()).doubleDigit()
    val hours = ((this / (1000 * 60 * 60) % 24).toInt()).doubleDigit()

    var result = "$hours:$minutes:$seconds"
    if (result.startsWith("00")) result = result.substring(3)
    return result
}

fun Int.doubleDigit(): String = if (this.toString().length == 1) "0$this"
else this.toString()
