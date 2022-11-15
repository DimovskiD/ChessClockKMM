package co.touchlab.kampkit.android.ui.composables.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.touchlab.kampkit.android.R
import co.touchlab.kampkit.android.ui.composables.ImageWithText
import co.touchlab.kampkit.android.ui.inverse
import co.touchlab.kampkit.android.ui.theme.Typography
import co.touchlab.kampkit.formatTime

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
            style = Typography.h4
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