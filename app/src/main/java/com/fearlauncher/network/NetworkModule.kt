package com.fearlauncher.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val MOJANG_BASE_URL = "https://piston-meta.mojang.com/"

    val minecraftApi: MinecraftApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MOJANG_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MinecraftApiService::class.java)
    }
}
