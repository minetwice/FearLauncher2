package com.fearlauncher.logic

import android.content.Context
import java.io.File

object VersionManager {

    fun getGameDirectory(context: Context): File {
        val dir = File(context.filesDir, ".minecraft")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getVersionsDirectory(context: Context): File {
        val dir = File(getGameDirectory(context), "versions")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun isVersionInstalled(context: Context, versionId: String): Boolean {
        val versionDir = File(getVersionsDirectory(context), versionId)
        val jsonFile = File(versionDir, "$versionId.json")
        val jarFile = File(versionDir, "$versionId.jar")
        return jsonFile.exists() && jarFile.exists()
    }

    suspend fun downloadVersion(
        context: Context,
        versionId: String,
        url: String,
        onProgress: (Float) -> Unit
    ) {
        // Real download logic with OkHttp and streaming to file
    }
}
