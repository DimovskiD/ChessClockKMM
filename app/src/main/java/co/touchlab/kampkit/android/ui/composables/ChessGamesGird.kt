package co.touchlab.kampkit.android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.kampkit.android.ui.composables.organisms.ChessGameGridCell
import co.touchlab.kampkit.android.ui.getColorForIndex
import co.touchlab.kampkit.db.ChessGame

private const val NUMBER_OF_COLUMNS = 2

@Composable
fun ChessGamesGrid(
    games: List<ChessGame>,
    onOpen: (ChessGame) -> Unit,
    onCreateNew: () -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(NUMBER_OF_COLUMNS) , content = {
        item { NewGameGridCell(onClick = onCreateNew) }
        items(games.size) { index ->
            val i = index + 1
            val row = i / NUMBER_OF_COLUMNS + 1
            val color = getColorForIndex(index, row)
            ChessGameGridCell(games[index], onOpen, color)
        }
        if ((games.size + 1) % 2 == 1) {
            item {
                Box(
                    modifier = Modifier
                        .background(
                            getColorForIndex(
                                games.size + 2,
                                (games.size + 1) / NUMBER_OF_COLUMNS + 1
                            )
                        )
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }
    })
}
