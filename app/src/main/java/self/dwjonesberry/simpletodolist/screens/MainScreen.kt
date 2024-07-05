package self.dwjonesberry.simpletodolist.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import self.dwjonesberry.simpletodolist.FirebaseRepository
import self.dwjonesberry.simpletodolist.Priority
import self.dwjonesberry.simpletodolist.Screens
import self.dwjonesberry.simpletodolist.TodoItem
import self.dwjonesberry.simpletodolist.TodoViewModel
import self.dwjonesberry.simpletodolist.TodoViewModelFactory
import self.dwjonesberry.simpletodolist.ui.theme.SimpleToDoListTheme

private val TAG: String = "MyProject:MainScreen"

@Composable
fun MainLayout(
    repo: FirebaseRepository = FirebaseRepository(),
    viewModel: TodoViewModel = viewModel(factory = TodoViewModelFactory(repo)),
) {
    val data by viewModel.todoList.collectAsState()
    val remembered = remember(data) { data }

    MainLayout(
        list = remembered,
        update = viewModel.update,
        deleteFromList = viewModel.delete,
        sort = viewModel.cycleSort,
        refresh = viewModel.refresh,
        sortedBy = viewModel.sortedBy,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainLayout(
    list: List<TodoItem>,
    update: (TodoItem) -> Unit,
    deleteFromList: (TodoItem) -> Unit,
    sort: () -> Unit,
    refresh: () -> Unit,
    sortedBy: Int,
) {
    val un = list.filter { !it.checked }
    val com = list.filter { it.checked }

    var filter by remember { mutableStateOf(0) }
    val setFilter: (Int) -> Unit = {
        filter = it
    }
    var unFiltered = listOf<TodoItem>()
    var comFiltered = listOf<TodoItem>()
    if (filter > 0) {
        unFiltered = un.filter { item ->
            item.priority.ordinal == filter
        }
        comFiltered = com.filter { item ->
            item.priority.ordinal == filter
        }
    } else {
        unFiltered = un
        comFiltered = com
    }

    val height = LocalConfiguration.current.screenHeightDp.dp

    Column(modifier = Modifier.height(height)) {
        MainActionBar(filter = setFilter, sort = sort, sortedBy = sortedBy)
        LazyColumn(
            modifier = Modifier
                .height(height)
        ) {
            item {
                MyLazyList(
                    modifier = Modifier.height(height),
                    heading = "Uncompleted",
                    list = unFiltered,
                    update = update,
                    deleteFromList = deleteFromList,
                    refresh = refresh
                )
                MyLazyList(
                    modifier = Modifier.height(height),
                    heading = "Completed",
                    list = comFiltered,
                    update = update,
                    deleteFromList = deleteFromList,
                    refresh = refresh
                )
            }
        }
    }
}

@Composable
private fun MainActionBar(filter: (Int) -> Unit, sort: () -> Unit, sortedBy: Int) {
    var sortedByText = "Sorted:"

    when (sortedBy) {
        0 -> sortedByText = "${sortedByText} IDup"
        1 -> sortedByText = "${sortedByText} IDdn"
        2 -> sortedByText = "${sortedByText} PRup"
        3 -> sortedByText = "${sortedByText} PRdn"
    }

    val filterN = { filter.invoke(0) }
    val filterL = { filter.invoke(1) }
    val filterM = { filter.invoke(2) }
    val filterH = { filter.invoke(3) }

    val buttons: List<Pair<String, List<() -> Unit>>> = listOf(
        Pair(sortedByText, listOf(sort)),
        Pair("N", listOf(filterN)),
        Pair("L", listOf(filterL)),
        Pair("M", listOf(filterM)),
        Pair("H", listOf(filterH))
    )

    Column() {
        ActionBar(buttons)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MyLazyList(
    modifier: Modifier,
    heading: String,
    list: List<TodoItem>,
    update: (TodoItem) -> Unit,
    deleteFromList: (TodoItem) -> Unit,
    refresh: () -> Unit,
) {
    val context = LocalContext.current
    var showSection: Boolean by remember { mutableStateOf(true) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { showSection = !showSection }
        .padding(10.dp)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(50.dp)
                .height(30.dp)
                .padding(horizontal = 5.dp)
        ) {
            if (!showSection) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    "Show section",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        SectionHeading(heading)
    }
    if (showSection) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            for (index in 0..<list.size) {
                ListItem(
                    item = list[index],
                    index = index,
                    update = update,
                    deleteFromList = deleteFromList,
                    refresh = refresh,
                )
            }

        }
    } else {
        Spacer(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp))
    }
}

@Composable
fun SectionHeading(heading: String) {
    Text(
        text = heading,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
    )
}

private fun changeBackground(checked: Boolean): Color {
    if (checked) return Color.LightGray
    else return Color.White
}

fun determineBorder(todoItem: TodoItem): Color {
    return when (todoItem.priority) {
        Priority.NORMAL -> {
            Color.Black
        }

        Priority.LOW -> {
            Color.Green
        }

        Priority.MEDIUM -> {
            Color.Blue
        }

        Priority.HIGH -> {
            Color.Red
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListItem(
    item: TodoItem,
    index: Int,
    update: (TodoItem) -> Unit,
    deleteFromList: (TodoItem) -> Unit,
    refresh: () -> Unit
) {
    var background by remember { mutableStateOf(Color.White) }
    var expanded by remember { mutableStateOf(false) }
    var border: Color by remember { mutableStateOf(Color.Black) }
    background = changeBackground(item.checked)
    border = determineBorder(item)
    Card(
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
                expanded = !expanded
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
                    maxLines =
                    if (!expanded) 1
                    else 10,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (expanded) {
                Column {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
                        Text(item.notes)
                    }
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
                        Button(
                            onClick = {
                                Log.d("MyProject", "edit button clicked on item #${item.id}")
                            }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit item number ${item.id}"
                            )
                        }
                        Button(
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
                        Button(
                            onClick = {
                                Log.d(
                                    "MyProject",
                                    "item #${index}: decrease priority button pressed"
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
                        Button(onClick = {
                            Log.d(TAG, "delete button pressed on item #${item.id}")
                            deleteFromList.invoke(item)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete item ${item.id}."
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun MainPreview() {
    val list = mutableListOf(TodoItem(0, "Hello"), TodoItem(1, "Goodbye"))
    SimpleToDoListTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MainLayout(list = listOf(
                TodoItem(id = 0, text = "Test 001", priority = Priority.NORMAL),
                TodoItem(id = 1, text = "Test 002", priority = Priority.LOW),
                TodoItem(id = 2, text = "Test 003", priority = Priority.MEDIUM),
                TodoItem(id = 3, text = "Test 004", priority = Priority.HIGH),
            ),
                deleteFromList = {}, update = {}, refresh = {}, sort = {}, sortedBy = 0
            )
        }
    }
}
