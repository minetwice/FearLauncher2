package com.fearlauncher.utils

import androidx.compose.runtime.mutableStateListOf
import com.fearlauncher.models.JavaRuntime

object RuntimeManager {
    val availableRuntimes = mutableStateListOf(
        JavaRuntime("JRE 18", 18, false, "https://github.com/adoptium/temurin18-binaries/releases/download/jdk-18.0.2.1%2B1/OpenJDK18U-jre_aarch64_linux_hotspot_18.0.2.1_1.tar.gz"),
        JavaRuntime("JRE 21", 21, false, "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.1%2B12/OpenJDK21U-jre_aarch64_linux_hotspot_21.0.1_12.tar.gz"),
        JavaRuntime("JRE 25", 25, false, "https://example.com/jre25-placeholder.tar.gz")
    )

    fun isRuntimeInstalled(version: Int): Boolean {
        return availableRuntimes.find { it.majorVersion == version }?.isInstalled ?: false
    }

    fun downloadRuntime(version: Int, onComplete: () -> Unit = {}) {
        val index = availableRuntimes.indexOfFirst { it.majorVersion == version }
        if (index != -1 && !availableRuntimes[index].isInstalled) {
            // In a real app, this would use DownloadManager or OkHttp
            // and extract to app's internal storage.
            val runtime = availableRuntimes[index]
            availableRuntimes[index] = runtime.copy(isInstalled = true)
            onComplete()
        }
    }

    fun autoDownloadMissingRuntimes() {
        // Automatically download essential runtimes on first launch
        downloadRuntime(18)
        downloadRuntime(21)
    }

    fun getRequiredRuntimeVersion(minecraftVersion: String): Int {
        return when {
            minecraftVersion.startsWith("1.21") || minecraftVersion.startsWith("1.20.5") || minecraftVersion.startsWith("1.20.6") -> 21
            minecraftVersion.startsWith("1.18") || minecraftVersion.startsWith("1.19") || minecraftVersion.startsWith("1.20") -> 18 // Using 18 as it satisfies 17+
            else -> 18 // Default to 18 for older versions in this context
        }
    }
}
