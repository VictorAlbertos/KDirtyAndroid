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

package app.presentation.sections.users.search

import app.data.sections.users.User
import app.data.sections.users.UserRepository
import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.widgets.Notifications
import app.presentation.sections.TransformationsMock
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class SearchUserPresenterTest {
    val transformations: Transformations = spy(TransformationsMock())
    val userRepository: UserRepository = mock()
    val notifications: Notifications = mock()
    val view: SearchUserPresenter.View = mock()
    lateinit var searchUserPresenterUT: SearchUserPresenter
    
    @Before fun init() {
        searchUserPresenterUT = SearchUserPresenter(userRepository, transformations, notifications)
        whenever(view.clicksSearchUser()).thenReturn(Observable.never())
    }

    @Test fun Verify_OnBindView() {
        searchUserPresenterUT.onBindView(view)

        verify(view).clicksSearchUser()
        verify(view, never()).showUser(any())
    }

    @Test fun When_Call_OnBindView_With_User_State_Then_ShowUser_Is_Called() {
        mockSuccessResponse()
        searchUserPresenterUT.onBindView(view)
        searchUserPresenterUT.getUserByUserName("noEmpty")
        verify(view).showUser(any())

        searchUserPresenterUT.onBindView(view)
        verify(view, times(2)).showUser(any())
    }

    @Test fun When_Call_GetUserByName_With_Empty_String_Then_ShowError() {
        searchUserPresenterUT.onBindView(view)
        searchUserPresenterUT.getUserByUserName("")

        verify(userRepository, never()).searchByUserName(any())
        verify(notifications).showSnackBar(any<Int>())
    }

    @Test fun Verify_GetUserByName_With_Success_Response() {
        mockSuccessResponse()
        searchUserPresenterUT.onBindView(view)
        searchUserPresenterUT.getUserByUserName("noEmpty")

        verify(notifications, never()).showSnackBar(any<Single<String>>())
        verify(transformations).safely<Any>()
        verify(transformations).loading<Any>()
        verify(transformations).reportOnSnackBar<Any>()
        verify(view).showUser(any())
    }

    @Test fun Verify_GetUserByName_With_Error_Response() {
        mockErrorResponse()
        searchUserPresenterUT.onBindView(view)
        searchUserPresenterUT.getUserByUserName("noEmpty")

        verify(transformations).safely<Any>()
        verify(transformations).loading<Any>()
        verify(transformations).reportOnSnackBar<Any>()

        verify(view, never()).showUser(any())
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
