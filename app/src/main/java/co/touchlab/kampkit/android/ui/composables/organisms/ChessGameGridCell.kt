package co.touchlab.kampkit.android.ui.composables.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.touchlab.kampkit.android.ui.composables.molecules.ChessGameDetailsRow
import co.touchlab.kampkit.android.ui.inverse
import co.touchlab.kampkit.db.ChessGame

@Composable
fun ChessGameGridCell(
    game: ChessGame,
    onClick: (ChessGame) -> Unit,
    color: Color,
    modifier: Modifier = Modifier
) {
    val inverseColor = color.inverse()
    Row(modifier = modifier
        .background(color)
        .fillMaxWidth()
        .aspectRatio(1f)
        .clickable { onClick(game) }, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(text = game.name, style = MaterialTheme.typography.h5, color = inverseColor)
            Spacer(modifier = Modifier.height(8.dp))
            ChessGameDetailsRow(game.time, game.increment, inverseColor)
        }
    }
}

@Preview
@Composable
private fun ChessGameGridCellPreview() {
    ChessGameGridCell(ChessGame(-1L, "Test", 1234, 2), onClick = { }, Color.White)
}