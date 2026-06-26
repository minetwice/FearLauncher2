package com.fearlauncher.models

import com.google.gson.annotations.SerializedName

data class MinecraftVersionManifest(
    @SerializedName("latest") val latest: Latest,
    @SerializedName("versions") val versions: List<Version>
)

data class Latest(
    @SerializedName("release") val release: String,
    @SerializedName("snapshot") val snapshot: String
)

data class Version(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String,
    @SerializedName("time") val time: String,
    @SerializedName("releaseTime") val releaseTime: String
)

data class FabricVersion(
    @SerializedName("version") val version: String,
    @SerializedName("stable") val stable: Boolean
)
