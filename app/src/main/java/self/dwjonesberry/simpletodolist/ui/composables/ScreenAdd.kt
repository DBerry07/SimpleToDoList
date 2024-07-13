package self.dwjonesberry.simpletodolist.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import self.dwjonesberry.simpletodolist.data.FirebaseRepository
import self.dwjonesberry.simpletodolist.data.TaskViewModel
import self.dwjonesberry.simpletodolist.data.TaskViewModelFactory


@Composable
fun AddTaskScreen(
    repo: FirebaseRepository = FirebaseRepository(),
    viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(repo)),
    navigateToMainScreen: () -> Unit
) {
    AddTaskLayout(
        holdingText = viewModel.text,
        holdingNotes = viewModel.notes,
        addToList = viewModel.add,
        setText = viewModel.setText,
        setNotes = viewModel.setNotes,
        navigateToMainScreen = navigateToMainScreen,
        update = viewModel.updateSelected,
        isEditing = if (viewModel.selectedTodo != null) true else false
    )
}

@Composable
private fun AddTaskLayout(
    holdingText: String,
    holdingNotes: String,
    addToList: () -> Unit,
    setText: (String) -> Unit,
    setNotes: (String) -> Unit,
    navigateToMainScreen: () -> Unit,
    update: () -> Unit,
    isEditing: Boolean,
) {
    Column {
        AddActionBar(addToList, update, navigateToMainScreen, isEditing)
        AddTodoText(holdingText, setText)
        AddTodoNotes(holdingNotes, setNotes)
    }
}

@Composable
private fun AddActionBar(
    addToList: () -> Unit,
    update: () -> Unit,
    navigateToMainScreen: () -> Unit,
    editing: Boolean
) {

    var buttons: List<Pair<@Composable () -> Unit, List<() -> Unit>>>? = null

    if (!editing) {
        val row: @Composable () -> Unit = {
            Row() {
//                Icon(Icons.Default.Add, "Add item to todo list")
                Text("Add")
            }
        }

        buttons = listOf(
            Pair(row, listOf(addToList, navigateToMainScreen)),
        )
    } else {
        val row: @Composable () -> Unit = {
            Row() {
//                Icon(Icons.Default.Add, "Add item to todo list")
                Text("Update")
            }
        }
        buttons = listOf(
            Pair(row, listOf(update, navigateToMainScreen)),
        )
    }
    ActionBar(buttons = buttons)
}

@Composable
private fun AddTodoText(holdingText: String, setText: (String) -> Unit) {
    var text by remember { mutableStateOf(holdingText) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp),
        label = { Text("Title") },
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        minLines = 2,
        maxLines = 2,
        value = text,
        onValueChange = {
            setText.invoke(it)
            text = it
        }
    )
}

@Composable
private fun AddTodoNotes(holdingNotes: String, setNotes: (String) -> Unit) {
    var notes by remember { mutableStateOf(holdingNotes) }

    OutlinedTextField(value = notes,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp),
        label = { Text("Notes") },
        minLines = 8,
        maxLines = 8,
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        onValueChange = {
            setNotes.invoke(it)
            notes = it
        })
}

@Preview
@Composable
fun ATSPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        AddTaskLayout("", "", {}, {}, {}, {}, {}, false)
    }
}