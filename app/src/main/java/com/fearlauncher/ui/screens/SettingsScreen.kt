package com.fearlauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fearlauncher.ui.theme.*
import com.fearlauncher.utils.RuntimeManager

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(GradientStart, GradientEnd)))
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Settings,
                contentDescription = null,
                tint = SilverAccent,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "Settings",
                    color = SilverAccent,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Configure your launcher environment",
                    color = SilverDark,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "JAVA RUNTIMES",
            color = SilverAccent,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                RuntimeManager.availableRuntimes.forEach { runtime ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                runtime.name,
                                color = SilverPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                if (runtime.isInstalled) "Installed" else "Not Downloaded",
                                color = if (runtime.isInstalled) SilverPrimary.copy(alpha = 0.7f) else Color.Red.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = { RuntimeManager.downloadRuntime(runtime.majorVersion) {} },
                            enabled = !runtime.isInstalled,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SilverPrimary,
                                disabledContainerColor = SilverDark.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            if (!runtime.isInstalled) {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                if (runtime.isInstalled) "INSTALLED" else "DOWNLOAD",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (runtime != RuntimeManager.availableRuntimes.last()) {
                        Divider(color = SilverDark.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}
