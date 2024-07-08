package self.dwjonesberry.simpletodolist.ui.composables

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import self.dwjonesberry.simpletodolist.data.DummyTodo
import self.dwjonesberry.simpletodolist.data.Priority
import self.dwjonesberry.simpletodolist.data.TodoItem


private val TAG: String = "MyProject:PopUp"

@Composable
fun ListItemPopUp(
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    todoItem: TodoItem,
    update: (TodoItem) -> Unit,
    delete: (TodoItem) -> Unit
) {
    var bgColour = Color.White
    var priorityColour = Color.Black

    if (todoItem.checked) {
        bgColour = Color.LightGray
    }
    when (todoItem.priority) {
        Priority.NORMAL -> priorityColour = Color.Black
        Priority.LOW -> priorityColour = Color.Green
        Priority.MEDIUM -> priorityColour = Color.Blue
        Priority.HIGH -> priorityColour = Color.Red
    }

    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp
    val dialogMinWidth = width - (width * 0.5).toInt()
    val dialogMaxWidth = width
    val dialogMaxHeight = height - (height * 0.1).toInt()
    val dialogMinHeight = height - (height * 0.9).toInt()

    Dialog(onDismissRequest = { onDismissRequest.invoke() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier
                .border(5.dp, priorityColour, shape = MaterialTheme.shapes.small)
                .widthIn(min = dialogMinWidth.dp, max = dialogMaxWidth.dp).heightIn(min = dialogMinHeight.dp, max = dialogMaxHeight.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = bgColour
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                PopUpTextDisplay(modifier = Modifier, todoItem = todoItem, priorityColour = priorityColour)
                PopUpActonBar(
                    modifier = Modifier,
                    item = todoItem,
                    update = update,
                    delete = delete,
                    onDismissRequest = onDismissRequest
                )
            }
        }
    }
}

@Composable
fun PopUpTextDisplay(modifier: Modifier, todoItem: TodoItem, priorityColour: Color) {
    Text(todoItem.text, fontWeight = FontWeight.Bold, fontSize = 25.sp, color = priorityColour)
    Spacer(modifier = Modifier.padding(5.dp))
    Column(
        modifier = Modifier
            .heightIn(50.dp, 500.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        Text(todoItem.notes, fontSize = 18.sp)
        Spacer(modifier = Modifier.padding(5.dp))
    }
    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
fun PopUpActonBar(
    modifier: Modifier,
    item: TodoItem,
    update: (TodoItem) -> Unit,
    delete: (TodoItem) -> Unit,
    onDismissRequest: () -> Unit
) {
    val width = LocalConfiguration.current.screenWidthDp
    val maxWidth = width
    val minWidth = width - (0.5 * width).toInt()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.widthIn(min = minWidth.dp, max = maxWidth.dp)
    ) {
        IconButton(
            onClick = {
                Log.d("MyProject", "edit button clicked on item #${item.id}")
            }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit item number ${item.id}"
            )
        }
        IconButton(
            onClick = {
                Log.d(
                    "MyProject",
                    "item #${item.id}: increase priority button pressed"
                )
                Log.d("MyProject", "current priority: ${item.priority.name}")
                item.increasePriority()
                update(item)
                Log.d("MyProject", "new priority: ${item.priority.name}")
            }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase priority of item number ${item.id}"
            )
        }
        IconButton(
            onClick = {
                Log.d(
                    TAG,
                    "item #${item.id}: decrease priority button pressed"
                )
                Log.d("MyProject", "current priority: ${item.priority.name}")
                item.decreasePriority()
                update(item)
                Log.d("MyProject", "new priority: ${item.priority.name}")
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease priority of item number ${item.id}"
            )
        }
        IconButton(onClick = {
            Log.d(TAG, "delete button pressed on item #${item.id}")
            delete.invoke(item)
            onDismissRequest.invoke()
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete item ${item.id}."
            )
        }
    }
}

@Preview
@Composable
fun ListItemPopUpPreview() {
    val item = DummyTodo

    Surface(modifier = Modifier.fillMaxSize()) {
        ListItemPopUp(modifier = Modifier, onDismissRequest = { /*TODO*/ }, todoItem = item, {}, {})
    }
}