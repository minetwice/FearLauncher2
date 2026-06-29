package com.fearlauncher.logic

import android.content.Context
import com.google.gson.Gson
import java.io.File

data class LauncherConfig(
    val selectedUsername: String = "",
    val accounts: List<Account> = emptyList(),
    val maxMemory: Int = 4096,
    val resolution: String = "1280x720",
    val renderer: String = "Holly Renderer",
    val javaPath: String = "JRE 21",
    val jvmArgs: String = "-XX:+UseG1GC -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8",
    val gameDir: String = "",
    val guiScale: Float = 1.0f,
    val keepOpen: Boolean = true,
    val enableGloss: Boolean = true,
    val autoUpdate: Boolean = true,
    val showFps: Boolean = false,
    val vsync: Boolean = true,
    val fastMath: Boolean = true,
    val lazyChunk: Boolean = false,
    val logNative: Boolean = true,
    val gles3: Boolean = true,
    val threadPriority: Int = 5
)

data class Account(
    val username: String,
    val uuid: String = "",
    val accessToken: String = "",
    val type: String = "OFFLINE"
)

object ConfigManager {
    private const val CONFIG_FILE = "launcher_config.json"
    private val gson = Gson()

    fun getConfig(context: Context): LauncherConfig {
        val file = File(context.filesDir, CONFIG_FILE)
        return if (file.exists()) {
            try {
                gson.fromJson(file.readText(), LauncherConfig::class.java)
            } catch (e: Exception) {
                LauncherConfig()
            }
        } else {
            LauncherConfig()
        }
    }

    fun saveConfig(context: Context, config: LauncherConfig) {
        val file = File(context.filesDir, CONFIG_FILE)
        file.writeText(gson.toJson(config))
    }

    fun updateConfig(context: Context, update: (LauncherConfig) -> LauncherConfig) {
        val current = getConfig(context)
        saveConfig(context, update(current))
    }
}
