

package org.notesapp.project.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.notesapp.project.network.NotesApiService
import org.notesapp.project.i18n.LocalLocalizationManager
import org.notesapp.project.i18n.Strings

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val localizationManager = LocalLocalizationManager.current
    val apiService = remember { NotesApiService.getInstance() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background decorative elements
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            
            // Draw decorative circles
            drawCircle(
                color = Color(0xFF6750A4).copy(alpha = 0.1f),
                radius = canvasWidth * 0.3f,
                center = Offset(-canvasWidth * 0.2f, canvasHeight * 0.2f)
            )
            drawCircle(
                color = Color(0xFF7C4DFF).copy(alpha = 0.08f),
                radius = canvasWidth * 0.4f,
                center = Offset(canvasWidth * 1.2f, canvasHeight * 0.8f)
            )
            drawCircle(
                color = Color(0xFF00BCD4).copy(alpha = 0.06f),
                radius = canvasWidth * 0.25f,
                center = Offset(canvasWidth * 0.8f, canvasHeight * 0.1f)
            )
        }
        Card(
            modifier = Modifier.width(360.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Login Title and Subtitle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = localizationManager.getString(Strings.loginTitle),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = localizationManager.getString(Strings.loginSubtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = { Text(localizationManager.getString(Strings.email)) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text(localizationManager.getString(Strings.password)) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        coroutineScope.launch {
                            try {
                                val result = apiService.login(email, password)
                                isLoading = false
                                result.fold(
                                    onSuccess = { onLoginSuccess() },
                                    onFailure = { exception ->
                                        errorMessage = when {
                                            exception.message?.contains("network", ignoreCase = true) == true ||
                                            exception.message?.contains("timeout", ignoreCase = true) == true ||
                                            exception.message?.contains("connection", ignoreCase = true) == true ->
                                                "Network error. Please check your internet connection."
                                            exception.message?.contains("401", ignoreCase = true) == true ||
                                            exception.message?.contains("unauthorized", ignoreCase = true) == true ->
                                                "Invalid email or password."
                                            exception.message?.contains("404", ignoreCase = true) == true ->
                                                "Server not found. Please try again later."
                                            else -> "Login failed: ${exception.message ?: "Unknown error"}"
                                        }
                                    }
                                )
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = when {
                                    e.message?.contains("network", ignoreCase = true) == true ||
                                    e.message?.contains("timeout", ignoreCase = true) == true ||
                                    e.message?.contains("connection", ignoreCase = true) == true ->
                                        "Network error. Please check your internet connection."
                                    e.message?.contains("json", ignoreCase = true) == true ->
                                        "Server response error. Please try again."
                                    else -> "Login failed: ${e.message ?: "Unexpected error occurred"}"
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                    shape = MaterialTheme.shapes.large
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            localizationManager.getString(Strings.loginButton),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = localizationManager.getString(Strings.noAccount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    TextButton(onClick = onNavigateToRegister) {
                        Text(localizationManager.getString(Strings.registerButton))
                    }
                }
            }
        }
    }
}
