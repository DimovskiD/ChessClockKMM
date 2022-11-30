package com.deluxe1.chessclock.android.ui.composables.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deluxe1.chessclock.android.R
import com.deluxe1.chessclock.android.ui.composables.atoms.ImageWithText
import com.deluxe1.chessclock.android.ui.inverse
import com.deluxe1.chessclock.formatTime

@Composable
fun ChessGamePlayerComponent(
    modifier: Modifier = Modifier,
    timeLeft: Long,
    numberOfMoves: Int,
    color: Color,
    onClick: () -> Unit
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .background(color)
        .clickable { onClick() }) {
        Text(
            text = timeLeft.formatTime(true),
            modifier = Modifier.align(Alignment.Center),
            color = color.inverse(),
            style = MaterialTheme.typography.h4
        )
        ImageWithText(
            painter = painterResource(id = R.drawable.ic_pawn_svgrepo_com),
            text = "Total moves: $numberOfMoves",
            color = color.inverse(),
            modifier = Modifier
                .align(
                    Alignment.BottomCenter
                )
                .padding(bottom = 8.dp)
        )
    }
}