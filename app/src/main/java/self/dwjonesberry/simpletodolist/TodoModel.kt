package self.dwjonesberry.simpletodolist

class TodoModel {

    companion object {
        val todoItems: MutableList<TodoItem> = getFromDatabase()

        fun getFromDatabase(): MutableList<TodoItem> {
            return mutableListOf()
        }
    }

    fun addToList(item: String) {
        todoItems.add(TodoItem(text = item))
    }

}