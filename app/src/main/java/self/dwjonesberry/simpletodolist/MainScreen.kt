package self.dwjonesberry.simpletodolist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import self.dwjonesberry.simpletodolist.ui.theme.SimpleToDoListTheme
import kotlin.coroutines.coroutineContext

@Composable
fun MainScreen(navigateToAdd: () -> Unit) {
    val viewModel: TodoViewModel = viewModel()
    MainScreen(list = viewModel.getItems.invoke(), navigateToAdd = navigateToAdd)
}

@Composable
fun MainScreen(list: MutableList<TodoItem>, navigateToAdd: () -> Unit) {
    Column {
        MainActionBar(navigateToAdd = navigateToAdd)
        MainLazyList(list = list)
    }
}

@Composable
fun MainActionBar(navigateToAdd: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navigateToAdd.invoke() }) {
            Text("Add Item")
        }
//        Button(onClick = { /*TODO*/ }) {
//            Text("Button 2")
//        }
    }
}

@Composable
fun MainLazyList(list: MutableList<TodoItem>) {
    val context = LocalContext.current
    LazyColumn {
        items(
            count = list.size
        ) {

            Box(
                modifier =
                Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .border(width = 1.dp, color = Color.Black)
                    .fillMaxWidth()
                    .clickable {
                        Toast.makeText(context, "Hello, world!", Toast.LENGTH_LONG).show()
                    }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                ) {

                    var str: String = (it + 1).toString()
                    if ((it + 1) < 10) {
                        str = "0$str"
                    }
                    str = "$str:"

                    Text(
                        modifier = Modifier.width(30.dp),
                        text = str
                    )
                    Text(
                        text = list[it].text
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    val list = mutableListOf(TodoItem("Hello"), TodoItem("Goodbye"))
    SimpleToDoListTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MainScreen(list, {})
        }
    }
}