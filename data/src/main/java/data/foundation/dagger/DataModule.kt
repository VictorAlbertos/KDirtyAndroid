/*
 * Copyright 2016 Victor Albertos
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
 */

package data.foundation.dagger

import dagger.Module
import dagger.Provides
import data.sections.users.GithubUsersApi
import io.reactivecache2.ReactiveCache
import io.victoralbertos.jolyglot.GsonSpeaker
import io.victoralbertos.jolyglot.JolyglotGenerics
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

val URL_BASE_GITHUB = "https://api.github.com"

/**
 * Resolve the dependencies for data layer.
 */
@Module
class DataModule(filesDir: File, stethoInterceptor: Interceptor) {
    private val retrofitGithub: Retrofit
    private val httpClient = OkHttpClient.Builder()
    private val jolyglotGenerics = GsonSpeaker()
    private val reactiveCache = ReactiveCache.Builder().using(filesDir, jolyglotGenerics)

    init {
        httpClient().networkInterceptors().add(stethoInterceptor)
        this.retrofitGithub = setUpRetrofit(URL_BASE_GITHUB)
    }

    private fun setUpRetrofit(url: String, interceptor: Interceptor? = null): Retrofit {
        interceptor?.let { httpClient().addInterceptor(it) }

        return Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient().build())
                .build()
    }

    @Singleton
    @Provides
    fun provideGithubApi() = retrofitGithub.create(GithubUsersApi::class.java)

    @Singleton
    @Provides
    fun httpClient() = httpClient

    @Singleton
    @Provides
    fun provideReactiveCache(): ReactiveCache = reactiveCache

    @Singleton
    @Provides
    fun provideJolyglot(): JolyglotGenerics = jolyglotGenerics
}
