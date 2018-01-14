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

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import data.sections.users.User
import data.sections.users.UserRepository
import io.reactivex.Observable
import org.base_app_android.R
import presentation.foundation.presenter.Presenter
import presentation.foundation.presenter.ViewPresenter
import presentation.foundation.transformations.Transformations
import presentation.foundation.widgets.Notifications
import javax.inject.Inject

class SearchUserPresenter @Inject constructor(private val userRepository: UserRepository,
                                              private val transformations: Transformations,
                                              private val notifications: Notifications) : Presenter<SearchUserPresenter.View>() {
    private var userState: User? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        userState?.let { view.showUser(it) }

        view.clicksSearchUser().subscribe { getUserByUserName(view.username()) }
    }

    fun getUserByUserName(username: String) {
        if (username.isEmpty()) {
            notifications.showSnackBar(R.string.fill_missing_fields)
            return
        }

        userRepository.searchByUserName(username)
                .compose(transformations.schedules())
                .compose(transformations.loading())
                .compose(transformations.reportOnSnackBar())
                .subscribe { user ->
                    userState = user
                    view.showUser(user)
                }
    }

    interface View : ViewPresenter {
        fun showUser(user: User)

        fun clicksSearchUser(): Observable<Unit>

        fun username(): String
    }
}
