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

package app.presentation.sections.users.detail

import app.data.sections.users.User
import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.widgets.Notifications
import app.presentation.sections.TransformationsMock
import app.presentation.sections.users.UsersWireframe
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class UserPresenterTest {
    val transformations: Transformations = spy(TransformationsMock())
    val wireframe: UsersWireframe = mock()
    val view: UserPresenter.View = mock()
    val notifications: Notifications = mock()
    lateinit var userPresenterUT: UserPresenter

    @Before fun init() {
        userPresenterUT = UserPresenter(transformations, wireframe,
                notifications)
    }

    @Test fun Verify_OnBindView_With_Success_Response() {
        whenever(wireframe.userScreen)
                .thenReturn(Single.just(User(1, "name", "avatar")))
        userPresenterUT.onBindView(view)

        verify(transformations).safely<Any>()
        verify(transformations).loading<Any>()
        verify(transformations).reportOnSnackBar<Any>()
        verify(view).showUser(any())
    }

    @Test fun Verify_OnBindView_With_Error_Response() {
        whenever(wireframe.userScreen)
                .thenReturn(Single.error<User>(RuntimeException()))
        userPresenterUT.onBindView(view)

        verify(transformations).safely<Any>()
        verify(transformations).loading<Any>()
        verify(transformations).reportOnSnackBar<Any>()
        verify(view, never()).showUser(any())
    }
}
