package com.fearlauncher.network

import com.fearlauncher.models.MinecraftVersionManifest
import retrofit2.http.GET

interface MinecraftApiService {
    @GET("mc/game/version_manifest_v2.json")
    suspend fun getVersionManifest(): MinecraftVersionManifest
}
