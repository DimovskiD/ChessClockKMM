package com.deluxe1.chessclock.android.ui.composables.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deluxe1.chessclock.android.R
import com.deluxe1.chessclock.android.ui.theme.ChessClockTheme
import com.deluxe1.chessclock.db.ChessGame

@Composable
fun ChessGameOptions(
    game: ChessGame,
    onStart: (ChessGame) -> Unit,
    onEdit: (ChessGame) -> Unit,
    onDelete: (ChessGame) -> Unit
) {
    Row {
        Button(
            onClick = { onEdit(game) },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ChessClockTheme.colors.primary
            ),
            modifier = Modifier.size(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = "Edit"
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Button(
            onClick = { onStart(game) },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ChessClockTheme.colors.primaryVariant
            ),
            modifier = Modifier.size(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_play_arrow_24),
                contentDescription = "Play"
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Button(
            onClick = { onDelete(game) },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ChessClockTheme.colors.error
            ),
            modifier = Modifier.size(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                contentDescription = "Delete"
            )
        }
    }
}