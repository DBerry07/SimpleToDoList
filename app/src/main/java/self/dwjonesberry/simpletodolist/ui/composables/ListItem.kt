package self.dwjonesberry.simpletodolist.ui.composables

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import self.dwjonesberry.simpletodolist.data.TodoItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    item: TodoItem,
    index: Int,
    update: (TodoItem) -> Unit,
    deleteFromList: (TodoItem) -> Unit,
) {
    var background by remember { mutableStateOf(Color.White) }
    var showDialog by remember { mutableStateOf(false) }
    var border: Color by remember { mutableStateOf(Color.Black) }
    background = changeBackground(item.checked)
    border = determineBorder(item)
    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors().copy(
            containerColor = background
        ),
        modifier =
        Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .border(2.dp, border, shape = MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .combinedClickable(onDoubleClick = {
                item.checked = !(item.checked)
                background = changeBackground(item.checked)
                Log.d("MyProject", "checked = ${item.checked}")
                update(item)
//                    refresh.invoke()
            }) {
                showDialog = !showDialog
                Log.d("MyProject", "item $index double clicked")
            }
    ) {
        Column() {
            Row(
                modifier =
                Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            )
            {
                var str: String = item.id.toString()
                if ((item.id) < 10) {
                    str = "0$str"
                }
                if ((item.id) < 100) {
                    str = "0$str"
                }
                str = "$str:"

                Text(
                    modifier = Modifier.width(60.dp),
                    text = str,
                    color = border,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = FontFamily.Monospace,
                )
                Text(
                    text = item.text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (showDialog) {
                ListItemPopUp(
                    modifier = Modifier,
                    onDismissRequest = { showDialog = !showDialog },
                    todoItem = item,
                    update = update,
                    delete = deleteFromList
                )
            }
        }
    }
}