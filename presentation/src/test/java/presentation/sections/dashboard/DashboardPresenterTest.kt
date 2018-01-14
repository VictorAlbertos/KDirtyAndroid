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

package presentation.sections.dashboard

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import org.base_app_android.R
import org.junit.Before
import org.junit.Test
import presentation.foundation.views.FragmentsManager
import presentation.sections.users.search.SearchUserFragment

class DashboardPresenterTest {
    private val dashboardView: DashboardPresenter.View = mock()
    private val fragmentsManager: FragmentsManager = mock()
    private val dashboardPresenterUT by lazy { DashboardPresenter(fragmentsManager).apply { view = dashboardView } }

    @Before
    fun init() {
        whenever(dashboardView.clicksItemSelected()).thenReturn(Observable.never())
    }

    @Test
    fun Verify_onCreate() {
        dashboardPresenterUT.onCreate()

        verify(dashboardView).clicksItemSelected()
        verify(dashboardView).replaceFragment(any(), any())
    }

    @Test
    fun Verify_onCreate_With_Current_Fragment() {
        dashboardPresenterUT.onCreate()

        verify(dashboardView).clicksItemSelected()
        verify(dashboardView).replaceFragment(any(), any())
    }

    @Test
    fun When_Call_replaceDrawerFragment_And_Current_Fragment_Is_The_Current_One_Then_Do_Not_Replace_It() {
        dashboardPresenterUT.onCreate()

        whenever(dashboardView.replaceFragment(any(), any())).thenReturn(false)
        dashboardPresenterUT.replaceDrawerFragment(R.id.drawer_users)

        verify(dashboardView, never()).setCheckedItemMenu(any())
        verify(dashboardView, never()).setTitleActionBar(any())
        verify(dashboardView, atLeastOnce()).replaceFragment(any(), any())
    }

    @Test
    fun When_Call_replaceDrawerFragment_And_Current_Fragment_Is_Not_The_Current_One_Then_Replace_It() {
        dashboardPresenterUT.onCreate()

        whenever(dashboardView.replaceFragment(any(), any())).thenReturn(true)
        dashboardPresenterUT.replaceDrawerFragment(R.id.drawer_find_user)

        verify(dashboardView).setCheckedItemMenu(R.id.drawer_find_user)
        verify(dashboardView).setTitleActionBar(R.string.find_user)
        verify(dashboardView).replaceFragment(fragmentsManager, SearchUserFragment::class.java)
    }
}
