package org.notesapp.project.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.notesapp.project.model.Note
import org.notesapp.project.model.NoteColor
import org.notesapp.project.utils.currentTimeMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    note: Note?,
    onSave: (Note) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var selectedColor by remember { mutableStateOf(note?.color ?: NoteColor.DEFAULT) }
    var showColorSheet by remember { mutableStateOf(false) }
    var isPinned by remember { mutableStateOf(note?.isPinned ?: false) }
    var showSaveAnimation by remember { mutableStateOf(false) }
    
    val sheetState = rememberModalBottomSheetState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (note != null) "Edit Note" else "New Note",
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Pin toggle
                    IconButton(onClick = { isPinned = !isPinned }) {
                        Icon(
                            imageVector = if (isPinned) Icons.Default.Star else Icons.Default.Add,
                            contentDescription = if (isPinned) "Unpin" else "Pin",
                            tint = if (isPinned) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    
                    // Color picker button
                    IconButton(onClick = { showColorSheet = true }) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(selectedColor.color)
                                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        )
                    }
                    
                    // Save button
                    AnimatedVisibility(
                        visible = title.isNotBlank() || content.isNotBlank(),
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        IconButton(
                            onClick = {
                                showSaveAnimation = true
                                val noteToSave = if (note != null) {
                                    note.copy(
                                        title = title,
                                        content = content,
                                        color = selectedColor,
                                        isPinned = isPinned,
                                        timestamp = currentTimeMillis()
                                    )
                                } else {
                                    Note(
                                        title = title,
                                        content = content,
                                        color = selectedColor,
                                        isPinned = isPinned
                                    )
                                }
                                onSave(noteToSave)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = selectedColor.lightColor.copy(alpha = 0.3f)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(selectedColor.lightColor.copy(alpha = 0.15f))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { 
                        Text(
                            "Title",
                            style = MaterialTheme.typography.headlineSmall
                        ) 
                    },
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.7f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                        focusedBorderColor = selectedColor.color,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Content Input
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { 
                        Text(
                            "Write your note here...",
                            style = MaterialTheme.typography.bodyLarge
                        ) 
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 26.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.7f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                        focusedBorderColor = selectedColor.color,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            
            // Save Animation
            SaveAnimation(
                visible = showSaveAnimation,
                onAnimationEnd = { showSaveAnimation = false }
            )
        }
    }
    
    // Color Picker Bottom Sheet
    if (showColorSheet) {
        ModalBottomSheet(
            onDismissRequest = { showColorSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            ColorPickerBottomSheet(
                selectedColor = selectedColor,
                onColorSelected = { 
                    selectedColor = it
                    showColorSheet = false
                }
            )
        }
    }
}

@Composable
fun ColorPickerBottomSheet(
    selectedColor: NoteColor,
    onColorSelected: (NoteColor) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // Header with drag handle
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(40.dp)
                .height(4.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(2.dp)
                )
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            "Choose Color",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )
        
        // Color grid
        val colors = NoteColor.entries.chunked(4)
        
        colors.forEach { rowColors ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowColors.forEach { color ->
                    ColorCircle(
                        color = color,
                        isSelected = color == selectedColor,
                        onClick = { onColorSelected(color) }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ColorCircle(
    color: NoteColor,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .size(70.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(if (isSelected) 64.dp else 56.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            color.lightColor,
                            color.color
                        )
                    )
                )
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 3.dp,
                            color = color.color,
                            shape = CircleShape
                        )
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = isSelected,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.25f),
                            shape = CircleShape
                        )
                        .padding(6.dp)
                )
            }
        }
    }
}

@Composable
fun SaveAnimation(
    visible: Boolean,
    onAnimationEnd: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            initialScale = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(),
        exit = scaleOut(
            targetScale = 1.3f,
            animationSpec = tween(durationMillis = 400)
        ) + fadeOut(
            animationSpec = tween(durationMillis = 400)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Saved",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(800)
            onAnimationEnd()
        }
    }
}
