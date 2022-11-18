package com.deluxe1.chessclock.android.ui

import androidx.compose.ui.graphics.Color

fun getColorForIndex(index: Int, row: Int): Color =
    if (row % 2 == 0) {
        if (index % 2 == 0) Color.White
        else Color.Black
    } else {
        if (index % 2 == 1) Color.White
        else Color.Black
    }

fun Color.inverse() =
    if (this == Color.White) Color.Black
    else Color.White