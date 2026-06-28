package com.fearlauncher.logic.skins

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

data class Skin(
    val name: String,
    val file: File,
    val isAlexModel: Boolean = false
)

object SkinManager {
    fun getSkinsDirectory(context: Context): File {
        val dir = File(context.filesDir, ".minecraft/skins")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getSkins(context: Context): List<Skin> {
        val dir = getSkinsDirectory(context)
        return dir.listFiles()?.filter { it.extension == "png" }?.map {
            Skin(it.nameWithoutExtension, it)
        } ?: emptyList()
    }

    fun importSkin(context: Context, uri: Uri, name: String) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val destination = File(getSkinsDirectory(context), "$name.png")
        inputStream?.use { input ->
            FileOutputStream(destination).use { output ->
                input.copyTo(output)
            }
        }
    }
}
