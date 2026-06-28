package com.fearlauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fearlauncher.logic.Account
import com.fearlauncher.logic.ConfigManager
import com.fearlauncher.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isLocalMode by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo/Title Section
        Text(
            "FEAR LAUNCHER",
            style = MaterialTheme.typography.headlineLarge,
            color = SilverAccent,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 36.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Minecraft Java Launcher",
            color = SilverDark,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(48.dp))

        // Login Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mode Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(DeepBlack, RoundedCornerShape(24.dp))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                if (isLocalMode) SilverPrimary else Color.Transparent,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { isLocalMode = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "LOCAL",
                            color = if (isLocalMode) BlackBg else SilverDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                if (!isLocalMode) SilverPrimary else Color.Transparent,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { isLocalMode = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "MICROSOFT",
                            color = if (!isLocalMode) BlackBg else SilverDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (isLocalMode) {
                    // Username Field
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username", color = SilverDark) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, "Person", tint = SilverPrimary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = SilverPrimary,
                            unfocusedBorderColor = SilverDark
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Local Login Button
                    Button(
                        onClick = {
                            if (username.isNotBlank()) {
                                isLoading = true
                                ConfigManager.updateConfig(context) { config ->
                                    val newAccount = Account(username)
                                    config.copy(
                                        selectedUsername = username,
                                        accounts = (config.accounts + newAccount).distinctBy { it.username }
                                    )
                                }
                                onLoginSuccess(username)
                                isLoading = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isLoading && username.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SilverPrimary,
                            disabledContainerColor = SilverDark
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = BlackBg,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "START PLAYING",
                                color = BlackBg,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            "Microsoft",
                            tint = SilverPrimary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Secure Microsoft Login",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                isLoading = true
                                val msUser = "MS_${(1000..9999).random()}"
                                ConfigManager.updateConfig(context) { config ->
                                    val newAccount = Account(msUser, type = "MICROSOFT")
                                    config.copy(
                                        selectedUsername = msUser,
                                        accounts = (config.accounts + newAccount).distinctBy { it.username }
                                    )
                                }
                                onLoginSuccess(msUser)
                                isLoading = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SilverPrimary
                            )
                        ) {
                            Text(
                                "LOGIN WITH MICROSOFT",
                                color = BlackBg,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "By continuing, you agree to our Terms of Service",
            color = SilverDark.copy(alpha = 0.6f),
            fontSize = 10.sp
        )
    }
}
