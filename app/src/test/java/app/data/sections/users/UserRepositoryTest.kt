/*
 * Copyright 2017 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package app.data.sections.users

import app.data.foundation.net.ErrorAdapter
import app.data.foundation.net.NetworkResponse
import io.reactivecache2.ReactiveCache
import io.reactivex.exceptions.CompositeException
import io.rx_cache2.Source
import io.victoralbertos.jolyglot.GsonSpeaker
import io.victoralbertos.mockery.api.Mockery
import io.victoralbertos.mockery.api.built_in_interceptor.Rx2Retrofit
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.IOException

class UserRepositoryTest {
    @get:Rule val testFolder = TemporaryFolder()
    private lateinit var userRepositoryUT: UserRepository

    @Test fun Verify_GetUsers_With_LastIdQueried_Null() {
        mockApiForSuccess()

        val observer = userRepositoryUT.getUsers(null, false).test()
        observer.awaitTerminalEvent()
        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it[0].id == 1 }
    }

    @Test fun Verify_GetUsers_Success() {
        mockApiForSuccess()

        val id = 50

        val observer = userRepositoryUT.getUsers(id, false).test()
        observer.awaitTerminalEvent()
        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it[0].id == 51 }
    }

    @Test fun Verify_GetUsers_Refresh() {
        mockApiForSuccess()

        val observer1 = userRepositoryUT.getUsersReply(null, false).test()
        observer1.awaitTerminalEvent()
        observer1.assertValue { it.source == Source.CLOUD }

        val observer2 = userRepositoryUT.getUsersReply(null, false).test()
        observer2.awaitTerminalEvent()
        observer2.assertValue { it.source == Source.MEMORY }

        val observer3 = userRepositoryUT.getUsersReply(null, true).test()
        observer3.awaitTerminalEvent()
        observer3.assertValue { it.source == Source.CLOUD }
    }

    @Test fun Verify_GetUsers_Failure() {
        mockApiForFailure()

        val observer = userRepositoryUT.getUsers(null, false).test()
        observer.awaitTerminalEvent()

        observer.assertError(CompositeException::class.java)
                .assertNoValues()
    }

    @Test fun Verify_SearchByUserName_Success() {
        mockApiForSuccess()

        val username = "username"

        val observer = userRepositoryUT.searchByUserName(username).test()
        observer.awaitTerminalEvent()
        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it.login == username }
    }

    @Test fun Verify_SearchByUserName_Failure() {
        mockApiForFailure()

        val observer = userRepositoryUT.searchByUserName("don't care").test()
        observer.awaitTerminalEvent()
        observer.assertError(IOException::class.java)
                .assertErrorMessage("Mock failure!")
                .assertNoValues()
    }

    @Rx2Retrofit(delay = 0, failurePercent = 0, variancePercentage = 0) internal interface ApiSuccess : GithubUsersApi

    @Rx2Retrofit(delay = 0, failurePercent = 100, variancePercentage = 0) internal interface ApiFailure : GithubUsersApi

    private fun mockApiForSuccess() {
        val networkResponse = NetworkResponse(ErrorAdapter(GsonSpeaker()))

        val githubUsersApi = Mockery.Builder<ApiSuccess>()
                .mock(ApiSuccess::class.java)
                .build()

        val reactiveCache = ReactiveCache.Builder()
                .using(testFolder.root, GsonSpeaker())

        userRepositoryUT = UserRepository(githubUsersApi,
                networkResponse, reactiveCache)
    }

    private fun mockApiForFailure() {
        val networkResponse = NetworkResponse(ErrorAdapter(GsonSpeaker()))

        val githubUsersApi = Mockery.Builder<ApiFailure>()
                .mock(ApiFailure::class.java)
                .build()

        val reactiveCache = ReactiveCache.Builder()
                .using(testFolder.root, GsonSpeaker())

        userRepositoryUT = UserRepository(githubUsersApi,
                networkResponse, reactiveCache)
    }
}
