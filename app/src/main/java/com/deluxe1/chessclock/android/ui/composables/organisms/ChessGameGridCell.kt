package com.deluxe1.chessclock.android.ui.composables.organisms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deluxe1.chessclock.android.R
import com.deluxe1.chessclock.android.ui.composables.molecules.ChessGameDetailsRow
import com.deluxe1.chessclock.android.ui.inverse
import com.deluxe1.chessclock.db.ChessGame

@Composable
fun ChessGameGridCell(
    game: ChessGame,
    onClick: () -> Unit,
    onStart: (ChessGame) -> Unit,
    onEdit: (ChessGame) -> Unit,
    onDelete: (ChessGame) -> Unit,
    color: Color,
    isShowingOptions: Boolean,
    modifier: Modifier = Modifier
) {
    val inverseColor = color.inverse()

    Row(modifier = modifier
        .background(color)
        .fillMaxWidth()
        .aspectRatio(1f)
        .clickable { onClick() }, verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (isShowingOptions) {
                Row {
                    Button(
                        onClick = { onEdit(game) },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Yellow
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
                            backgroundColor = Color.Gray
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
                            backgroundColor = Color.Red
                        ),
                        modifier = Modifier.size(50.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                            contentDescription = "Delete"
                        )
                    }
                }
            } else {
                Text(text = game.name, style = MaterialTheme.typography.h5, color = inverseColor)
                Spacer(modifier = Modifier.height(8.dp))
                ChessGameDetailsRow(game.time, game.increment, inverseColor)
            }
        }
    }
}

@Preview
@Composable
private fun ChessGameGridCellPreview() {
    ChessGameGridCell(
        ChessGame(-1L, "Test", 1234, 2),
        onClick = { },
        onStart = {},
        onDelete = {},
        onEdit = {},
        color = Color.White,
        isShowingOptions = false
    )
}