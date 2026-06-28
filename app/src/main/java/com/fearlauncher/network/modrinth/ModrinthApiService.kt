package com.fearlauncher.network.modrinth

import retrofit2.http.GET
import retrofit2.http.Query

interface ModrinthApiService {
    @GET("search")
    suspend fun searchProjects(
        @Query("query") query: String,
        @Query("facets") facets: String = "[[\"project_type:modpack\"]]",
        @Query("limit") limit: Int = 20
    ): ModrinthSearchResponse
}

data class ModrinthSearchResponse(
    val hits: List<ModrinthProject>
)

data class ModrinthProject(
    val project_id: String,
    val title: String,
    val description: String,
    val icon_url: String?,
    val downloads: Int,
    val follows: Int
)
