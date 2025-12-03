package org.notesapp.project.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.notesapp.project.i18n.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(modifier: Modifier = Modifier) {
    val localizationManager = LocalLocalizationManager.current
    val currentLanguage by localizationManager.currentLanguage
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier.menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Language,
                    contentDescription = localizationManager.getString(Strings.language),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = currentLanguage.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Language.entries.forEach { language ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            language.displayName,
                            color = if (language == currentLanguage) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        localizationManager.setLanguage(language)
                        expanded = false
                    },
                    leadingIcon = if (language == currentLanguage) {
                        { 
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    } else null
                )
            }
        }
    }
}
