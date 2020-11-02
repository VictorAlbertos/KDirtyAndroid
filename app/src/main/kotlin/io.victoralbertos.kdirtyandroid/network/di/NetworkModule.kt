package io.victoralbertos.kdirtyandroid.network.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.victoralbertos.kdirtyandroid.network.api.GithubUsersApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    private const val URL_BASE_GITHUB = "https://api.github.com"

    @Provides
    fun provideUsersApi(): GithubUsersApi {
        return Retrofit.Builder()
            .baseUrl(URL_BASE_GITHUB)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()
            .create(GithubUsersApi::class.java)
    }
}
