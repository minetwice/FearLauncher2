package com.fearlauncher.network

import com.fearlauncher.network.modrinth.ModrinthApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val MOJANG_BASE_URL = "https://piston-meta.mojang.com/"
    private const val MODRINTH_API_URL = "https://api.modrinth.com/v2/"

    val minecraftApi: MinecraftApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MOJANG_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MinecraftApiService::class.java)
    }

    val modrinthApi: ModrinthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MODRINTH_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ModrinthApiService::class.java)
    }
}
