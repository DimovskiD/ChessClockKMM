package com.deluxe1.chessclock.android.ui.composables.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.deluxe1.chessclock.android.ui.composables.ChessGamesGrid
import com.deluxe1.chessclock.db.ChessGame

@Composable
fun EmptyChessGamePicker(
    onAddNew: () -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth(), columns = GridCells.Fixed(2)
    ) {
        item {
            NewGameGridCell(onAddNew)
        }
    }
}

@Composable
fun ErrorChessGamePicker(error: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = error)
    }
}

@Composable
fun SuccessChessGamePicker(
    successData: List<ChessGame>,
    onOpen: (ChessGame) -> Unit,
    onEdit: (ChessGame) -> Unit,
    onDelete: (ChessGame) -> Unit,
    onCreateNew: () -> Unit
) {
    ChessGamesGrid(games = successData, onOpen, onCreateNew = onCreateNew, onEdit = onEdit, onDelete = onDelete)
}
