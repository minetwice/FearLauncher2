@file:OptIn(ExperimentalMaterial3Api::class)
package com.fearlauncher.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.fearlauncher.logic.VersionManager
import com.fearlauncher.network.NetworkModule
import com.fearlauncher.ui.theme.*
import kotlinx.coroutines.launch

data class MinecraftVersion(
    val id: String,
    val name: String,
    val type: String,
    val isInstalled: Boolean = false,
    val loader: String = "Vanilla" // Vanilla, Fabric, Forge, Optifine, Quilt, NeoForge
)

@Composable
fun PlayScreen(
    onLaunchGame: (String) -> Unit
) {
    var selectedVersion by remember { mutableStateOf<MinecraftVersion?>(null) }
    var expandedDropdown by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }
    var downloadStatus by remember { mutableStateOf<com.fearlauncher.logic.VersionManager.DownloadStatus?>(null) }
    var availableVersions by remember { mutableStateOf<List<MinecraftVersion>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val manifest = NetworkModule.minecraftApi.getVersionManifest()
            availableVersions = manifest.versions.map { v ->
                MinecraftVersion(
                    id = v.id,
                    name = v.id,
                    type = v.type,
                    isInstalled = VersionManager.isVersionInstalled(context, v.id),
                    loader = "Vanilla"
                )
            }
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "PLAY MINECRAFT",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SilverAccent,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Select your version and launch",
                    color = SilverDark,
                    fontSize = 14.sp
                )
            }
            
            // Version Selector Dropdown
            Box {
                OutlinedButton(
                    onClick = { expandedDropdown = true },
                    modifier = Modifier.width(220.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SilverPrimary
                    )
                ) {
                    Icon(Icons.Default.List, "Versions", tint = SilverPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        selectedVersion?.id ?: "Select Version",
                        color = SilverPrimary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, null, tint = SilverPrimary)
                }

                DropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false },
                    modifier = Modifier.background(BlackSurface).width(220.dp)
                ) {
                    availableVersions.take(10).forEach { version ->
                        DropdownMenuItem(
                            text = { Text(version.id, color = SilverPrimary) },
                            onClick = {
                                selectedVersion = version
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Content Area
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Left Panel - Version List
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        "Available Versions",
                        style = MaterialTheme.typography.titleMedium,
                        color = SilverAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        placeholder = { Text("Search versions...", color = SilverDark) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = SilverPrimary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = SilverPrimary,
                            unfocusedBorderColor = SilverDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableVersions.filter {
                            it.id.contains(searchQuery, ignoreCase = true) &&
                            VersionManager.isVersionInstalled(context, it.id)
                        }) { version ->
                            VersionListItem(
                                version = version,
                                isSelected = selectedVersion?.id == version.id,
                                onSelect = { selectedVersion = version }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Right Panel - Launch Controls
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selectedVersion != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Large Loader Icon and Text
                            LoaderInfoSection(selectedVersion!!.loader)

                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                selectedVersion!!.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = SilverPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                selectedVersion!!.type,
                                color = SilverDark,
                                fontSize = 12.sp
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Memory Allocation
                            Text(
                                "Memory Allocation",
                                color = SilverDark,
                                fontSize = 12.sp
                            )
                            Slider(
                                value = 4f,
                                onValueChange = { },
                                valueRange = 2f..8f,
                                steps = 5,
                                colors = SliderDefaults.colors(
                                    thumbColor = SilverPrimary,
                                    activeTrackColor = SilverPrimary,
                                    inactiveTrackColor = SilverDark
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("2GB", color = SilverDark, fontSize = 10.sp)
                                Text("4GB", color = SilverPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("8GB", color = SilverDark, fontSize = 10.sp)
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Download Status Bar
                            if (isDownloading && downloadStatus != null) {
                                com.fearlauncher.ui.components.DragonEpicDownloadBar(status = downloadStatus!!)
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            // RUN Button
                            Button(
                                onClick = {
                                    if (VersionManager.isVersionInstalled(context, selectedVersion!!.id)) {
                                        onLaunchGame(selectedVersion!!.id)
                                    } else {
                                        isDownloading = true
                                        scope.launch {
                                            try {
                                                val manifest = NetworkModule.minecraftApi.getVersionManifest()
                                                val versionInfo = manifest.versions.find { it.id == selectedVersion!!.id }
                                                val detail = NetworkModule.minecraftApi.getVersionDetail(versionInfo?.url ?: "")

                                                VersionManager.downloadVersion(
                                                    context = context,
                                                    versionId = selectedVersion!!.id,
                                                    clientJarUrl = detail.downloads.client.url,
                                                    onStatus = { status ->
                                                        downloadStatus = status
                                                    }
                                                )
                                                availableVersions = availableVersions.map {
                                                    if (it.id == selectedVersion!!.id) it.copy(isInstalled = true) else it
                                                }
                                                selectedVersion = selectedVersion?.copy(isInstalled = true)
                                            } catch (e: Exception) {
                                                // Handle error
                                            } finally {
                                                isDownloading = false
                                                downloadStatus = null
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                enabled = !isLoading && !isDownloading,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (VersionManager.isVersionInstalled(context, selectedVersion!!.id)) Color(0xFFC0C0C0) else SilverDark
                                )
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(28.dp),
                                        color = BlackBg,
                                        strokeWidth = 3.dp
                                    )
                                } else {
                                    val isInstalled = VersionManager.isVersionInstalled(context, selectedVersion!!.id)
                                    Icon(
                                        if (isInstalled) Icons.Default.PlayArrow else Icons.Default.Download,
                                        "Action",
                                        tint = BlackBg,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        if (isInstalled) "RUN" else "INSTALL",
                                        color = BlackBg,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Select a version to continue",
                                color = SilverDark,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Actions
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            "Mod Loaders",
                            style = MaterialTheme.typography.titleSmall,
                            color = SilverAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            LoaderSmallIcon("Fabric")
                            LoaderSmallIcon("Forge")
                            LoaderSmallIcon("Quilt")
                            LoaderSmallIcon("NeoForge")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoaderInfoSection(loader: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(DeepBlack, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                loader.take(1).uppercase(),
                color = SilverPrimary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            loader,
            color = SilverAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            when(loader) {
                "Fabric" -> "Lightweight mod loader"
                "Forge" -> "Classic modding API"
                "Optifine" -> "Optimization and Shaders"
                "Quilt" -> "Community-driven loader"
                "NeoForge" -> "The new Forge era"
                else -> "Official game engine"
            },
            color = SilverDark,
            fontSize = 11.sp
        )
    }
}

@Composable
fun LoaderSmallIcon(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(DeepBlack, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(name.take(1), color = SilverPrimary, fontWeight = FontWeight.Bold)
        }
        Text(name, color = SilverDark, fontSize = 9.sp)
    }
}

@Composable
fun VersionListItem(
    version: MinecraftVersion,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SilverPrimary.copy(alpha = 0.2f) 
                          else BlackSurface.copy(alpha = 0.5f)
        ),
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    version.name,
                    color = if (isSelected) SilverPrimary else Color.White,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp
                )
                Text(
                    version.loader,
                    color = SilverDark,
                    fontSize = 11.sp
                )
            }
            
            if (VersionManager.isVersionInstalled(context, version.id)) {
                Icon(
                    Icons.Default.CheckCircle,
                    "Installed",
                    tint = SilverPrimary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    Icons.Default.Download,
                    "Download",
                    tint = SilverDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
