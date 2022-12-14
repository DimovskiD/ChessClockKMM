package com.deluxe1.chessclock.android.ui.composables.molecules

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.deluxe1.chessclock.android.R
import com.deluxe1.chessclock.android.ui.composables.atoms.ImageWithText
import com.deluxe1.chessclock.formatTime

@Composable
fun ChessGameDetailsRow(
    duration: Long,
    increment: Long,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        ImageWithText(
            painter = painterResource(id = R.drawable.ic_baseline_hourglass_top_24),
            text = duration.formatTime(false),
            modifier = Modifier.weight(1f),
            color = color
        )
        ImageWithText(
            painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_up_24),
            text = increment.toString(),
            modifier = Modifier.weight(1f),
            color = color
        )
    }
}