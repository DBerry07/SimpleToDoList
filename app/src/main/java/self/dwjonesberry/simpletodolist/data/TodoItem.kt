package self.dwjonesberry.simpletodolist.data

class TodoItem(
    val id: Int,
    var text: String = "",
    var notes: String = "",
    var checked: Boolean = false,
    var priority: Priority = Priority.NORMAL
) {
    fun increasePriority() {
        val current = this.priority.ordinal
        if (current < Priority.entries.size - 1) {
            this.priority = Priority.entries[current + 1]
        }
    }

    fun decreasePriority() {
        val current = this.priority.ordinal
        if (current > 0) {
            this.priority = Priority.entries[current - 1]
        }
    }

    override fun toString(): String {
        return "TODO:: id:${id.toString()}, text:${text}, notes:${notes}, priority:${priority.name.toString()}"
    }
}

enum class Priority {
    NORMAL,
    LOW,
    MEDIUM,
    HIGH,
}