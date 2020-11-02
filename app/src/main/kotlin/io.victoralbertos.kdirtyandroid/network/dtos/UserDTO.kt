package io.victoralbertos.kdirtyandroid.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    val id: Int,
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String?
)
