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

package app.presentation.sections.users.list

import android.support.annotation.VisibleForTesting
import app.data.sections.users.User
import app.data.sections.users.UserRepository
import app.presentation.foundation.presenter.Presenter
import app.presentation.foundation.presenter.ViewPresenter
import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.widgets.Notifications
import app.presentation.sections.users.UsersWireframe
import io.reactivex.Observable
import miguelbcr.ok_adapters.recycler_view.Pager
import javax.inject.Inject

class UsersPresenter @Inject constructor(private val repository: UserRepository,
                                         private val wireframe: UsersWireframe,
                                         transformations: Transformations,
                                         notifications: Notifications) : Presenter<UsersPresenter.View>(transformations, notifications) {

    @VisibleForTesting val usersState: MutableList<User> = mutableListOf()

    override fun onBindView(view: View) {
        super.onBindView(view)

        view.setUpLoaderPager(usersState, Pager.LoaderPager<User> { lastUser ->
            Pager.Call<User> { nextPage(lastUser, it) }
        })

        view.setUpRefreshList(Pager.Call<User> { this.refreshList(it) })

        view.userSelectedClicks()
                .flatMap { user -> wireframe.userScreen(user).toObservable<Any>() }
                .subscribe()
    }

    @VisibleForTesting fun nextPage(user: User?, callback: Pager.Callback<User>) {
        repository.getUsers(user?.id, false)
                .compose(transformations.safely<List<User>>())
                .compose(transformations.reportOnSnackBar<List<User>>())
                .subscribe { users ->
                    callback.supply(users)
                    usersState.addAll(users)
                }
    }

    @VisibleForTesting fun refreshList(callback: Pager.Callback<User>) {
        repository.getUsers(null, true)
                .compose(transformations.safely<List<User>>())
                .compose(transformations.reportOnSnackBar<List<User>>())
                .subscribe { users ->
                    callback.supply(users)
                    usersState.clear()
                    usersState.addAll(users)
                    view.hideLoadingOnRefreshList()
                }
    }

    interface View : ViewPresenter {
        fun setUpLoaderPager(initialLoad: List<User>, loaderPager: Pager.LoaderPager<User>)

        fun setUpRefreshList(call: Pager.Call<User>)

        fun userSelectedClicks(): Observable<User>

        fun hideLoadingOnRefreshList()
    }
}
