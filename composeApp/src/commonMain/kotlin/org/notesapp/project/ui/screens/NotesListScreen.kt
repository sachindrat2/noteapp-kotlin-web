package org.notesapp.project.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.notesapp.project.model.Note
import org.notesapp.project.viewmodel.NotesViewModel
import org.notesapp.project.utils.formatRelativeTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    viewModel: NotesViewModel,
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val filteredNotes = remember(viewModel.notes, viewModel.searchQuery) {
        viewModel.getFilteredNotes()
    }
    
    var searchExpanded by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            NotesTopAppBar(
                searchQuery = viewModel.searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                searchExpanded = searchExpanded,
                onSearchExpandedChange = { searchExpanded = it },
                isGridView = viewModel.isGridView,
                onViewModeToggle = { viewModel.toggleViewMode() },
                onLogout = onLogout
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Decorative background
            DecorativeBackground()
            
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Error message snackbar
                viewModel.errorMessage?.let { error ->
                    Snackbar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        action = {
                            TextButton(onClick = { viewModel.loadNotesFromApi() }) {
                                Text("Retry")
                            }
                        }
                    ) {
                        Text(error)
                    }
                }
                
                // Loading indicator
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                if (filteredNotes.isEmpty() && !viewModel.isLoading) {
                    EmptyNotesView(
                        hasNotes = viewModel.notes.isNotEmpty(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (!viewModel.isLoading) {
                    AnimatedContent(
                        targetState = viewModel.isGridView,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                        }
                    ) { isGrid ->
                        if (isGrid) {
                            NotesGridView(
                                notes = filteredNotes,
                                onNoteClick = onNoteClick,
                                onPinToggle = { viewModel.togglePin(it) },
                                onDelete = { viewModel.deleteNote(it) }
                            )
                        } else {
                            NotesListView(
                                notes = filteredNotes,
                                onNoteClick = onNoteClick,
                                onPinToggle = { viewModel.togglePin(it) },
                                onDelete = { viewModel.deleteNote(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopAppBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchExpanded: Boolean,
    onSearchExpandedChange: (Boolean) -> Unit,
    isGridView: Boolean,
    onViewModeToggle: () -> Unit,
    onLogout: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column {
            TopAppBar(
                title = {
                    AnimatedContent(
                        targetState = searchExpanded,
                        transitionSpec = {
                            slideInHorizontally { -it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                        }
                    ) { expanded ->
                        if (expanded) {
                            TextField(
                                value = searchQuery,
                                onValueChange = onSearchQueryChange,
                                placeholder = { Text("Search notes...") },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                "My Notes",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { onSearchExpandedChange(!searchExpanded) }) {
                        Icon(
                            imageVector = if (searchExpanded) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = if (searchExpanded) "Close Search" else "Search"
                        )
                    }
                    IconButton(onClick = onViewModeToggle) {
                        Icon(
                            imageVector = if (isGridView) Icons.Default.List else Icons.Default.Menu,
                            contentDescription = "Toggle View"
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

@Composable
fun NotesGridView(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onPinToggle: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = notes, key = { it.id }) { note ->
            NoteCard(
                note = note,
                onClick = { onNoteClick(note) },
                onPinToggle = { onPinToggle(note.id) },
                onDelete = { onDelete(note.id) }
            )
        }
    }
}

@Composable
fun NotesListView(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onPinToggle: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = notes, key = { it.id }) { note ->
            NoteCard(
                note = note,
                onClick = { onNoteClick(note) },
                onPinToggle = { onPinToggle(note.id) },
                onDelete = { onDelete(note.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onPinToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    val scaleAnim by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    
    Card(
        modifier = modifier
            .scale(scaleAnim)
            .clickable {
                scale = 0.95f
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = note.color.lightColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        LaunchedEffect(scale) {
            if (scale == 0.95f) {
                kotlinx.coroutines.delay(100)
                scale = 1f
            }
        }
        
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (note.title.isNotEmpty()) {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (note.content.isNotEmpty()) {
                        Text(
                            text = note.content,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 8,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                if (note.isPinned) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Pinned",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(start = 8.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatRelativeTime(note.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onPinToggle,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (note.isPinned) Icons.Default.Star else Icons.Default.Star,
                            contentDescription = if (note.isPinned) "Unpin" else "Pin",
                            tint = if (note.isPinned) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyNotesView(
    hasNotes: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (hasNotes) Icons.Default.Search else Icons.Default.Create,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(60.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = if (hasNotes) "No notes found" else "No notes yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (hasNotes) 
                "Try a different search term" 
            else 
                "Tap the + button to create your first note",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun DecorativeBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.06f),
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f)
                    )
                )
            )
    ) {
        // Background decorative elements using Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            
            // Draw multiple decorative circles with different colors and positions
            drawCircle(
                color = Color(0xFF6750A4).copy(alpha = 0.12f),
                radius = canvasWidth * 0.35f,
                center = Offset(-canvasWidth * 0.15f, canvasHeight * 0.25f)
            )
            
            drawCircle(
                color = Color(0xFF7C4DFF).copy(alpha = 0.10f),
                radius = canvasWidth * 0.45f,
                center = Offset(canvasWidth * 1.2f, canvasHeight * 0.6f)
            )
            
            drawCircle(
                color = Color(0xFF00BCD4).copy(alpha = 0.08f),
                radius = canvasWidth * 0.3f,
                center = Offset(canvasWidth * 0.8f, canvasHeight * 0.1f)
            )
            
            drawCircle(
                color = Color(0xFF4CAF50).copy(alpha = 0.09f),
                radius = canvasWidth * 0.25f,
                center = Offset(canvasWidth * 0.2f, canvasHeight * 0.8f)
            )
            
            drawCircle(
                color = Color(0xFFFF9800).copy(alpha = 0.07f),
                radius = canvasWidth * 0.4f,
                center = Offset(canvasWidth * 0.9f, canvasHeight * 0.9f)
            )
            
            drawCircle(
                color = Color(0xFFE91E63).copy(alpha = 0.06f),
                radius = canvasWidth * 0.28f,
                center = Offset(canvasWidth * 0.5f, -canvasHeight * 0.1f)
            )
        }
    }
}
