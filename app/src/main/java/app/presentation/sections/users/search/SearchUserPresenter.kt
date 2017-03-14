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

import android.support.annotation.VisibleForTesting
import app.data.sections.users.User
import app.data.sections.users.UserRepository
import app.presentation.foundation.presenter.Presenter
import app.presentation.foundation.presenter.ViewPresenter
import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.widgets.Notifications
import io.reactivex.Observable
import org.base_app_android.R
import javax.inject.Inject

class SearchUserPresenter @Inject constructor(private val userRepository: UserRepository,
                                              transformations: Transformations,
                                              notifications: Notifications) : Presenter<SearchUserPresenter.View>(transformations, notifications) {
    private var userState: User? = null

    override fun onBindView(view: View) {
        super.onBindView(view)
        if (userState != null) {
            view.showUser(userState!!)
        }

        view.clicksSearchUser().subscribe { getUserByUserName(view.username()) }
    }

    @VisibleForTesting fun getUserByUserName(username: String) {
        if (username.isEmpty()) {
            notifications.showSnackBar(R.string.fill_missing_fields)
            return
        }

        userRepository.searchByUserName(username)
                .compose(transformations.safely<User>())
                .compose(transformations.loading<User>())
                .compose(transformations.reportOnSnackBar<User>())
                .subscribe { user ->
                    userState = user
                    view.showUser(user)
                }
    }

    interface View : ViewPresenter {
        fun showUser(user: User)

        fun clicksSearchUser(): Observable<Any>

        fun username(): String
    }
}
