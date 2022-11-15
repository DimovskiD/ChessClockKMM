package co.touchlab.kampkit.android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NewGameGridCell(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .background(Color.White)
        .fillMaxWidth()
        .aspectRatio(1f)
        .clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Add, contentDescription = "Add", Modifier.size(130.dp))
            Text(text = "Custom Game", style = MaterialTheme.typography.h5)
        }
    }
}

@Preview
@Composable
fun NewGameGridCellPreview() {
    NewGameGridCell(onClick = { })
}