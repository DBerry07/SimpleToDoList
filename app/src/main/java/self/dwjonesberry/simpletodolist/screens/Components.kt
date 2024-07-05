package self.dwjonesberry.simpletodolist.screens

import android.util.Log
import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun ActionBar(buttons: List<Pair<String, List<() -> Unit>>>) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        for (pair in buttons) {
            Button(onClick = {
                for (function in pair.second) {
                    function.invoke()
                }
            }) {
                Text(pair.first)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAppBar(title: String, buttons: List< @Composable () -> Unit>) {
    val TAG = "TodoAppBar"
    TopAppBar(
        title = { Text(title) },
        navigationIcon = { buttons[0].invoke() },
        actions = {
            for (index in 1..<buttons.size) {
                buttons[index].invoke()
            }
        }
    )
}

@Composable
fun AppBarButton(function: () -> Unit, icon: ImageVector, description: String) {
    IconButton(onClick = { function.invoke() }) {
        Icon(icon, description)
    }
}