package io.victoralbertos.kdirtyandroid.network.api

import io.victoralbertos.kdirtyandroid.network.dtos.UserDTO
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubUsersApi {
    @Headers(HEADER_API_VERSION)
    @GET("/users")
    suspend fun getUsers(
        @Query("since") lastIdQueried: Int,
        @Query("per_page") perPage: Int
    ): List<UserDTO>

    companion object {
        private const val HEADER_API_VERSION = "Accept: application/vnd.github.v3+json"
    }
}
