/*
 * Copyright 2017 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*


package app.data.sections.users

import app.data.foundation.net.ErrorAdapter
import app.data.sections.users.UsersMockery.UserDTO
import io.reactivex.Single
import io.victoralbertos.mockery.api.built_in_interceptor.Rx2Retrofit
import io.victoralbertos.mockery.api.built_in_mockery.DTOArgs
import io.victoralbertos.mockery.api.built_in_mockery.Optional
import io.victoralbertos.mockery.api.built_in_mockery.Valid
import io.victoralbertos.mockery.api.built_in_mockery.Valid.Template.STRING
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

*/
/**
 * Definition for Retrofit and Mockery of every endpoint required by the Api.
 *//*

@Rx2Retrofit(delay = 2000, errorResponseAdapter = ErrorAdapter::class)
interface GithubUsersApi {
    companion object {
        const val HEADER_API_VERSION = "Accept: application/vnd.github.v3+json"
    }

    @Headers(HEADER_API_VERSION)
    @GET("/users/{username}")
    @DTOArgs(UserDTO::class)
    fun getUserByName(
            @Valid(value = STRING, legal = "google") @Path("username") username: String): Single<Response<User>>

    @Headers(HEADER_API_VERSION)
    @GET("/users")
    @DTOArgs(UserDTO::class)
    fun getUsers(
            @Optional @Query("since") lastIdQueried: Int,
            @Optional @Query("per_page") perPage: Int): Single<Response<List<User>>>
}


*/
