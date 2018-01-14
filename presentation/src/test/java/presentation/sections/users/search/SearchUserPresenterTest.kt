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
 */

package presentation.sections.users.search

import com.nhaarman.mockito_kotlin.*
import data.sections.users.User
import data.sections.users.UserRepository
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import presentation.foundation.transformations.Transformations
import presentation.foundation.widgets.Notifications
import presentation.sections.TransformationsMock

class SearchUserPresenterTest {
    private val userRepository: UserRepository = mock()
    private val transformations: Transformations = spy(TransformationsMock())
    private val notifications: Notifications = mock()
    private val viewSearch: SearchUserPresenter.View = mock()

    private val searchUserPresenterUT by lazy {
        SearchUserPresenter(userRepository, transformations, notifications).apply { view = viewSearch }
    }

    @Before
    fun before() {
        whenever(viewSearch.clicksSearchUser()).thenReturn(Observable.never())
    }

    @Test
    fun Verify_OnCreate() {
        searchUserPresenterUT.onCreate()

        verify(viewSearch).clicksSearchUser()
        verify(viewSearch, never()).showUser(any())
    }

    @Test
    fun When_Call_OnCreate_With_User_State_Then_ShowUser_Is_Called() {
        mockSuccessResponse()

        searchUserPresenterUT.onCreate()

        searchUserPresenterUT.getUserByUserName("noEmpty")
        verify(viewSearch).showUser(any())

        searchUserPresenterUT.onCreate()

        verify(viewSearch, times(2)).showUser(any())
    }

    @Test
    fun When_Call_GetUserByName_With_Empty_String_Then_ShowError() {
        searchUserPresenterUT.onCreate()

        searchUserPresenterUT.getUserByUserName("")

        verify(userRepository, never()).searchByUserName(any())
        verify(notifications).showSnackBar(any<Int>())
    }

    @Test
    fun Verify_GetUserByName_With_Success_Response() {
        mockSuccessResponse()
        searchUserPresenterUT.onCreate()

        searchUserPresenterUT.getUserByUserName("noEmpty")

        verify(notifications, never()).showSnackBar(any<Single<String>>())
        verify(transformations).schedules<Any>()
        verify(transformations).loading<Any>()
        verify(transformations).reportOnSnackBar<Any>()
        verify(viewSearch).showUser(any())
    }

    @Test
    fun Verify_GetUserByName_With_Error_Response() {
        mockErrorResponse()
        searchUserPresenterUT.onCreate()
        searchUserPresenterUT.getUserByUserName("noEmpty")

        verify(transformations).schedules<Any>()
        verify(transformations).loading<Any>()
        verify(transformations).reportOnSnackBar<Any>()

        verify(viewSearch, never()).showUser(any())
    }

    private fun mockSuccessResponse() {
        whenever(userRepository.searchByUserName(any()))
                .thenReturn(Single.just(User(1, "user", "avatar")))
    }

    private fun mockErrorResponse() {
        whenever(userRepository.searchByUserName(any()))
                .thenReturn(Single.error<User>(RuntimeException()))
    }

}
