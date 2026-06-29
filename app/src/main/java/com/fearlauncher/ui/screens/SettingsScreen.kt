package com.fearlauncher.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fearlauncher.ui.theme.*
import com.fearlauncher.logic.ConfigManager

@Composable
fun SettingsScreen() {
    val scrollState = rememberScrollState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val config = remember { ConfigManager.getConfig(context) }

    // Settings State
    var javaPath by remember { mutableStateOf(config.javaPath) }
    var jvmArgs by remember { mutableStateOf(config.jvmArgs) }
    var memoryAlloc by remember { mutableFloatStateOf(config.maxMemory / 1024f) }
    var resolution by remember { mutableStateOf(config.resolution) }
    var gameDir by remember { mutableStateOf(config.gameDir) }
    var renderer by remember { mutableStateOf(config.renderer) }
    var guiScale by remember { mutableFloatStateOf(config.guiScale) }
    var keepOpen by remember { mutableStateOf(config.keepOpen) }
    var enableGloss by remember { mutableStateOf(config.enableGloss) }
    var autoUpdate by remember { mutableStateOf(config.autoUpdate) }
    var showFps by remember { mutableStateOf(config.showFps) }
    var vsync by remember { mutableStateOf(config.vsync) }
    var fastMath by remember { mutableStateOf(config.fastMath) }
    var lazyChunk by remember { mutableStateOf(config.lazyChunk) }
    var logNative by remember { mutableStateOf(config.logNative) }
    var gles3 by remember { mutableStateOf(config.gles3) }
    var threadPriority by remember { mutableFloatStateOf(config.threadPriority.toFloat()) }

    LaunchedEffect(javaPath, jvmArgs, memoryAlloc, resolution, gameDir, renderer, guiScale,
                   keepOpen, enableGloss, autoUpdate, showFps, vsync, fastMath, lazyChunk, logNative, gles3, threadPriority) {
        val ramMB = (memoryAlloc * 1024).toInt()
        ConfigManager.updateConfig(context) { it.copy(
            javaPath = javaPath,
            jvmArgs = jvmArgs,
            maxMemory = ramMB,
            resolution = resolution,
            gameDir = gameDir,
            renderer = renderer,
            guiScale = guiScale,
            keepOpen = keepOpen,
            enableGloss = enableGloss,
            autoUpdate = autoUpdate,
            showFps = showFps,
            vsync = vsync,
            fastMath = fastMath,
            lazyChunk = lazyChunk,
            logNative = logNative,
            gles3 = gles3,
            threadPriority = threadPriority.toInt()
        )}
        com.fearlauncher.core.java.CoreConfig.saveRamSettings(context.filesDir, ramMB)
        com.fearlauncher.core.java.CoreConfig.saveJreSettings(context.filesDir, javaPath)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = guiScale
                scaleY = guiScale
            }
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Text(
            "SETTINGS",
            style = MaterialTheme.typography.headlineMedium,
            color = SilverAccent,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Global launcher configurations",
            color = SilverDark,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        SettingsSection(title = "Java Runtime") {
            SettingsDropdown(
                label = "Java Version",
                options = listOf("JRE 18", "JRE 21", "JRE 25"),
                selected = javaPath,
                onSelect = { javaPath = it }
            )
            SettingsTextField(label = "JVM Arguments", value = jvmArgs, onValueChange = { jvmArgs = it })
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Game Settings") {
            SettingsSlider(label = "Global Memory Allocation", value = memoryAlloc, range = 2f..16f, onValueChange = { memoryAlloc = it })

            ResolutionSetting(resolution = resolution, onResolutionChange = { resolution = it })

            SettingsSlider(label = "GUI Scale", value = guiScale, range = 0.5f..2f, onValueChange = { guiScale = it }, isInteger = false, isGuiScale = true)
            SettingsTextField(label = "Game Directory", value = gameDir, onValueChange = { gameDir = it })
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Video Settings") {
            SettingsDropdown(
                label = "Renderer",
                options = listOf("Holly Renderer", "Zink", "LTW Renderer", "GL4ES 1.1.4"),
                selected = renderer,
                onSelect = { renderer = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Launcher") {
            SettingsToggle(label = "Keep launcher open", enabled = keepOpen, onToggle = { keepOpen = it })
            SettingsToggle(label = "Enable dark mode gloss", enabled = enableGloss, onToggle = { enableGloss = it })
            SettingsToggle(label = "Auto-update assets", enabled = autoUpdate, onToggle = { autoUpdate = it })
            SettingsToggle(label = "Show FPS counter", enabled = showFps, onToggle = { showFps = it })
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Performance") {
            SettingsToggle(label = "Enable VSync", enabled = vsync, onToggle = { vsync = it })
            SettingsToggle(label = "Use Fast Math", enabled = fastMath, onToggle = { fastMath = it })
            SettingsToggle(label = "Lazy Chunk Loading", enabled = lazyChunk, onToggle = { lazyChunk = it })
            SettingsSlider(label = "Thread Priority", value = threadPriority, range = 1f..10f, onValueChange = { threadPriority = it })
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Advanced Debug") {
            SettingsToggle(label = "Log Native Output", enabled = logNative, onToggle = { logNative = it })
            SettingsToggle(label = "Enable GLES3", enabled = gles3, onToggle = { gles3 = it })
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun ResolutionSetting(resolution: String, onResolutionChange: (String) -> Unit) {
    Column {
        Text("Resolution", color = SilverDark, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    val res = resolution.split("x")
                    if (res.size == 2) {
                        val w = (res[0].toInt() - 80).coerceAtLeast(640)
                        val h = (res[1].toInt() - 45).coerceAtLeast(360)
                        onResolutionChange("${w}x${h}")
                    }
                },
                modifier = Modifier.background(DeepBlack, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Remove, null, tint = SilverPrimary)
            }

            Text(
                resolution,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.animateContentSize()
            )

            IconButton(
                onClick = {
                    val res = resolution.split("x")
                    if (res.size == 2) {
                        val w = (res[0].toInt() + 80).coerceAtMost(3840)
                        val h = (res[1].toInt() + 45).coerceAtMost(2160)
                        onResolutionChange("${w}x${h}")
                    }
                },
                modifier = Modifier.background(DeepBlack, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Add, null, tint = SilverPrimary)
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            title,
            color = SilverPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, color = SilverDark, fontSize = 12.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SilverPrimary,
                unfocusedBorderColor = SilverDark.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun SettingsSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    isInteger: Boolean = true,
    isGuiScale: Boolean = false
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = SilverDark, fontSize = 12.sp)
            Text(
                if (isInteger) "${value.toInt()}" else String.format("%.2f", value),
                color = SilverPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        // Flame Dragon Animation for slider would be a custom component
        // For now we improve the visual of the standard slider
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = SilverPrimary,
                activeTrackColor = SilverPrimary,
                inactiveTrackColor = SilverDark.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
fun SettingsDropdown(label: String, options: List<String>, selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, color = SilverDark, fontSize = 12.sp)
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(SilverDark, SilverPrimary)))
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(selected)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(BlackSurface)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = SilverPrimary) },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsToggle(label: String, enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = SilverDark, fontSize = 14.sp)
        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SilverPrimary,
                checkedTrackColor = SilverPrimary.copy(alpha = 0.5f),
                uncheckedThumbColor = SilverDark,
                uncheckedTrackColor = DeepBlack
            )
        )
    }
}
