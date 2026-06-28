package com.fearlauncher.logic

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

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

    data class DownloadStatus(
        val progress: Float,
        val downloadedSize: Long,
        val totalSize: Long,
        val speed: Double, // Bytes per second
        val etaSeconds: Long
    )

    suspend fun downloadVersion(
        context: Context,
        versionId: String,
        clientJarUrl: String,
        onStatus: (DownloadStatus) -> Unit
    ) = withContext(Dispatchers.IO) {
        val versionDir = File(getVersionsDirectory(context), versionId)
        if (!versionDir.exists()) versionDir.mkdirs()

        val destination = File(versionDir, "$versionId.jar")

        val client = OkHttpClient()
        val request = Request.Builder().url(clientJarUrl).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@withContext

            val body = response.body ?: return@withContext
            val contentLength = body.contentLength()

            val startTime = System.currentTimeMillis()
            body.byteStream().use { input ->
                FileOutputStream(destination).use { output ->
                    val buffer = ByteArray(16384)
                    var bytesRead: Int
                    var totalBytesRead = 0L

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead

                        val currentTime = System.currentTimeMillis()
                        val duration = (currentTime - startTime) / 1000.0
                        val speed = if (duration > 0) totalBytesRead / duration else 0.0
                        val remainingBytes = contentLength - totalBytesRead
                        val eta = if (speed > 0) (remainingBytes / speed).toLong() else 0L

                        if (contentLength > 0) {
                            onStatus(DownloadStatus(
                                progress = totalBytesRead.toFloat() / contentLength,
                                downloadedSize = totalBytesRead,
                                totalSize = contentLength,
                                speed = speed,
                                etaSeconds = eta
                            ))
                        }
                    }
                }
            }
        }
    }
}
