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

package app.presentation.sections.dashboard

import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.views.FragmentsManager
import app.presentation.foundation.widgets.Notifications
import app.presentation.sections.TransformationsMock
import app.presentation.sections.users.search.SearchUserFragment
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import org.base_app_android.R
import org.junit.Before
import org.junit.Test

class DashboardPresenterTest {
    val transformations: Transformations = spy(TransformationsMock())
    val view: DashboardPresenter.View = mock()
    val notifications: Notifications = mock()
    val fragmentsManager: FragmentsManager = mock()
    lateinit var dashboardPresenterUT: DashboardPresenter

    @Before fun init() {
        dashboardPresenterUT = DashboardPresenter(transformations, notifications, fragmentsManager)
        whenever(view.clicksItemSelected()).thenReturn(Observable.never())
    }

    @Test fun Verify_OnBindView() {
        dashboardPresenterUT.onBindView(view)

        verify(view).clicksItemSelected()
        verify(view).replaceFragment(any(), any())
    }

    @Test fun Verify_OnBindView_With_Current_Fragment() {
        dashboardPresenterUT.onBindView(view)

        verify(view).clicksItemSelected()
        verify(view).replaceFragment(any(), any())
    }

    @Test fun When_Call_replaceDrawerFragment_And_Current_Fragment_Is_The_Current_One_Then_Do_Not_Replace_It() {
        dashboardPresenterUT.onBindView(view)
        whenever(view.replaceFragment(any(), any())).thenReturn(false)
        dashboardPresenterUT.replaceDrawerFragment(R.id.drawer_users)

        verify(view, never()).setCheckedItemMenu(any())
        verify(view, never()).setTitleActionBar(any())
        verify(view, atLeastOnce()).replaceFragment(any(), any())
    }

    @Test fun When_Call_replaceDrawerFragment_And_Current_Fragment_Is_Not_The_Current_One_Then_Replace_It() {
        dashboardPresenterUT.onBindView(view)
        whenever(view.replaceFragment(any(), any())).thenReturn(true)
        dashboardPresenterUT.replaceDrawerFragment(R.id.drawer_find_user)

        verify(view).setCheckedItemMenu(R.id.drawer_find_user)
        verify(view).setTitleActionBar(R.string.find_user)
        verify(view).replaceFragment(fragmentsManager, SearchUserFragment::class.java)
    }
}
