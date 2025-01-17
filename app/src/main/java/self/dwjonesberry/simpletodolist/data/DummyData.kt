package self.dwjonesberry.simpletodolist.data

val DummyTodo: MyTask = MyTask(
    id = 0,
    title = "Hello, World!",
    notes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non gravida nulla. Maecenas at lectus orci. Vivamus ut posuere odio. Suspendisse potenti. Phasellus elit orci, vehicula fringilla est id, auctor ullamcorper turpis. Morbi vestibulum dui dolor, in viverra dolor condimentum ac. Nulla augue libero, consectetur at placerat vitae, posuere ultricies sapien. Donec ut tellus eu urna tincidunt gravida egestas vitae erat. Integer eros metus, varius at quam blandit, dapibus hendrerit orci. Vivamus massa turpis, maximus vestibulum nisl et, scelerisque tempus nisl. Donec id purus purus. Ut fermentum, elit in ultricies hendrerit, mauris eros luctus purus, in cursus sapien diam ut sem. Aliquam ornare diam nunc, a facilisis tortor imperdiet vitae. Sed tempor non dui vel pretium. Phasellus ut massa ex.\n Nam imperdiet eros urna, vestibulum commodo tortor tincidunt ac. Sed dictum eu elit vitae auctor. Vestibulum leo nunc, commodo quis interdum eget, eleifend vitae enim. Ut vitae sem convallis mauris cursus congue ut id neque. Proin ac mollis felis. In quam dui, placerat vitae feugiat in, porta vitae libero. In eu nisl non neque eleifend volutpat. Morbi convallis eros at est tempus auctor. Donec auctor libero sed libero maximus, facilisis pretium magna convallis. Vestibulum id tristique enim. Donec tincidunt, lacus sit amet commodo consectetur, leo lacus condimentum justo, ut aliquet lorem massa ac mauris. Donec euismod tempor mi. Sed sed tellus ultricies, tempus metus eleifend, dignissim ipsum. Nullam nisi dolor, accumsan eget faucibus id, rhoncus a felis.",
    checked = false,
    priority = Priority.NORMAL,
)

val DummyList: List<MyTask> = listOf(
    MyTask(id = 0, title = "Test 001", priority = Priority.NORMAL),
    MyTask(id = 1, title = "Test 002", priority = Priority.LOW),
    MyTask(id = 2, title = "Test 003", priority = Priority.MEDIUM),
    MyTask(id = 3, title = "Test 004", priority = Priority.HIGH),
    DummyTodo,
    MyTask(id = 4, title = "Test 005", priority = Priority.NORMAL, checked = true),
    MyTask(id = 5, title = "Test 006", priority = Priority.LOW, checked = true),
    MyTask(id = 6, title = "Test 007", priority = Priority.MEDIUM, checked = true),
    MyTask(id = 7, title = "Test 008", priority = Priority.HIGH, checked = true),
)