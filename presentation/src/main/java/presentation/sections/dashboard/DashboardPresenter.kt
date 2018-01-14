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

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.view.MenuItem
import io.reactivex.Observable
import org.base_app_android.R
import presentation.foundation.presenter.Presenter
import presentation.foundation.presenter.ViewPresenter
import presentation.foundation.views.FragmentsManager
import presentation.sections.users.list.UsersFragment
import presentation.sections.users.search.SearchUserFragment
import javax.inject.Inject

class DashboardPresenter @Inject constructor(private val fragmentsManager: FragmentsManager) : Presenter<DashboardPresenter.View>() {

    private val ITEMS_MENU = mapOf(
            R.id.drawer_users to ItemMenu(UsersFragment::class.java, R.string.users),
            R.id.drawer_find_user to ItemMenu(SearchUserFragment::class.java, R.string.find_user)
    )

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if (!onCreatedWasCalled) {
            replaceDrawerFragment(R.id.drawer_users)
        }

        view.clicksItemSelected()
                .subscribe { menuItem ->
                    replaceDrawerFragment(menuItem.itemId)
                    view.closeDrawer()
                }
    }

    @VisibleForTesting
    fun replaceDrawerFragment(@IdRes idSelectedMenu: Int) {
        val itemMenu = ITEMS_MENU[idSelectedMenu]
        val classFragment = itemMenu!!.classFragment

        if (view.replaceFragment(fragmentsManager, classFragment)) {
            view.setCheckedItemMenu(idSelectedMenu)
            view.setTitleActionBar(itemMenu.resTitle)
        }
    }

    interface View : ViewPresenter {

        fun replaceFragment(fragmentsManager: FragmentsManager,
                            classFragment: Class<out Fragment>): Boolean

        fun clicksItemSelected(): Observable<MenuItem>

        fun setCheckedItemMenu(@IdRes id: Int)

        fun setTitleActionBar(@StringRes id: Int)

        fun closeDrawer()
    }
}
