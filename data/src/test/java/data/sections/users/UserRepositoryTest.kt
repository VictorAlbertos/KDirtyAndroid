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

package data.sections.users

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import data.foundation.net.NetworkException
import data.foundation.net.NetworkResponse
import io.reactivecache2.ReactiveCache
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException
import io.rx_cache2.Source
import io.victoralbertos.jolyglot.GsonSpeaker
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import retrofit2.Response

class UserRepositoryTest {
    @get:Rule
    val testFolder = TemporaryFolder()
    private val reactiveCache by lazy { ReactiveCache.Builder().using(testFolder.root, GsonSpeaker()) }
    private val usersApi = mock<GithubUsersApi>()

    private val userRepositoryUT by lazy {
        UserRepository(usersApi, NetworkResponse(GsonSpeaker()), reactiveCache)
    }

    @Test
    fun Verify_GetUsers_With_LastIdQueried_Null() {
        mockApiForSuccess()

        val observer = userRepositoryUT.getUsers(null, false).test()
        observer.awaitTerminalEvent()
        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it.items[0].id == 1 }
    }

    @Test
    fun Verify_GetUsers_Success() {
        mockApiForSuccess()

        val id = 50

        val observer = userRepositoryUT.getUsers(id, false).test()
        observer.awaitTerminalEvent()
        observer.assertNoErrors()
                .assertValueCount(1)
    }

    @Test
    fun Verify_GetUsers_Refresh() {
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

    @Test
    fun Verify_GetUsers_Failure() {
        mockApiForFailure()

        val observer = userRepositoryUT.getUsers(null, false).test()
        observer.awaitTerminalEvent()

        observer.assertError(CompositeException::class.java)
                .assertNoValues()
    }

    @Test
    fun Verify_SearchByUserName_Success() {
        mockApiForSuccess()

        val observer = userRepositoryUT.searchByUserName("name").test()
        observer.awaitTerminalEvent()
        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it.login == "name" }
    }

    @Test
    fun Verify_SearchByUserName_Failure() {
        mockApiForFailure()

        val observer = userRepositoryUT.searchByUserName("don't care").test()
        observer.awaitTerminalEvent()
        observer.assertError(NetworkException::class.java)
                .assertNoValues()
    }

    private fun mockApiForSuccess() {
        whenever(usersApi.getUserByName(any())).thenReturn(Single.just(Response.success(User(1, "name", null))))
        whenever(usersApi.getUsers(any(), any())).thenReturn(Single.just(Response.success(listOf(User(1, "name", null)))))

    }

    private fun mockApiForFailure() {
        whenever(usersApi.getUserByName(any())).thenReturn(Single.just(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), ""))))
        whenever(usersApi.getUsers(any(), any())).thenReturn(Single.just(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), ""))))
    }
}
