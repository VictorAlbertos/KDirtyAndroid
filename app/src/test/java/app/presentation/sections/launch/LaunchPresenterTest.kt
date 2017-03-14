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

package app.presentation.sections.launch

import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.widgets.Notifications
import app.presentation.sections.TransformationsMock
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test

class LaunchPresenterTest {
    val transformations: Transformations = spy(TransformationsMock())
    lateinit var launchPresenterUT: LaunchPresenter
    val wireframe: LaunchWireframe = mock()
    val view: LaunchPresenter.View = mock()
    val notifications: Notifications = mock()

    @Before fun init() {
        launchPresenterUT = LaunchPresenter(transformations, wireframe, notifications)
    }

    @Test fun Verify_OnBindView() {
        whenever(wireframe.dashboard()).thenReturn(Completable.complete())
        launchPresenterUT.onBindView(view)

        verify(wireframe).dashboard()
    }
}
