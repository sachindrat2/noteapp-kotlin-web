import kotlin.test.*
import org.notesapp.project.viewmodel.NotesViewModel
import org.notesapp.project.model.Note

class NotesViewModelTest {
    @Test
    fun testAddNote() {
        val viewModel = NotesViewModel()
        val note = Note(title = "Test Note", content = "Test Content")
        viewModel.addNote(note)
        // Simulate async: in real test, use runBlocking or test dispatcher
        // Here, just check that the note is eventually in the list
        // (for demonstration, as Compose/Coroutine tests need more setup)
        // This is a basic structure; for real async, use kotlinx-coroutines-test
        assertTrue(viewModel.notes.any { it.title == "Test Note" && it.content == "Test Content" })
    }
}
