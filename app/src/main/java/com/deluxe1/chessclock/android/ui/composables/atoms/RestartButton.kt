package com.deluxe1.chessclock.android.ui.composables.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.deluxe1.chessclock.android.R
import com.deluxe1.chessclock.android.ui.theme.ChessClockTheme

@Composable
fun RestartButton(
    onRestart: () -> Unit
) {
    IconButton(
        onClick = { onRestart() },
        modifier = Modifier
            .background(ChessClockTheme.colors.error, CircleShape)
            .clickable { onRestart() }
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.ic_baseline_restart_alt_24
            ),
            contentDescription = null
        )
    }
}