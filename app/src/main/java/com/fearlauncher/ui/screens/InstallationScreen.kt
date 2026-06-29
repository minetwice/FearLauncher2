package com.fearlauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fearlauncher.logic.VersionManager
import com.fearlauncher.network.NetworkModule
import com.fearlauncher.ui.components.DragonEpicDownloadBar
import com.fearlauncher.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun InstallationScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var availableVersions by remember { mutableStateOf<List<MinecraftVersion>>(emptyList()) }
    var downloadingVersionId by remember { mutableStateOf<String?>(null) }
    var downloadStatus by remember { mutableStateOf<VersionManager.DownloadStatus?>(null) }

    LaunchedEffect(Unit) {
        try {
            val manifest = NetworkModule.minecraftApi.getVersionManifest()
            availableVersions = manifest.versions.map { v ->
                MinecraftVersion(v.id, "${v.id} - ${v.type}", v.type)
            }
        } catch (e: Exception) {}
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("INSTALLATIONS", style = MaterialTheme.typography.headlineMedium, color = SilverAccent, fontWeight = FontWeight.Bold)
        Text("Manage and download Minecraft versions", color = SilverDark, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search versions...", color = SilverDark) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = SilverPrimary) },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = SilverPrimary, unfocusedBorderColor = SilverDark),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (downloadingVersionId != null && downloadStatus != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlackSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DragonEpicDownloadBar(status = downloadStatus!!)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val filtered = availableVersions.filter { it.id.contains(searchQuery, ignoreCase = true) }
            items(filtered) { version ->
                val isInstalled = VersionManager.isVersionInstalled(context, version.id)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(version.id, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(version.type, color = SilverDark, fontSize = 12.sp)
                        }
                        if (isInstalled) {
                            Text("INSTALLED", color = SilverPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        } else {
                            Button(
                                onClick = {
                                    downloadingVersionId = version.id
                                    scope.launch {
                                        try {
                                            val manifest = NetworkModule.minecraftApi.getVersionManifest()
                                            val vInfo = manifest.versions.find { it.id == version.id }
                                            val detail = NetworkModule.minecraftApi.getVersionDetail(vInfo?.url ?: "")
                                            VersionManager.downloadVersion(context, version.id, detail.downloads.client.url) {
                                                downloadStatus = it
                                            }
                                        } finally {
                                            downloadingVersionId = null
                                            downloadStatus = null
                                        }
                                    }
                                },
                                enabled = downloadingVersionId == null,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SilverPrimary)
                            ) {
                                Icon(Icons.Default.Download, null, tint = BlackBg, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("INSTALL", color = BlackBg, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
