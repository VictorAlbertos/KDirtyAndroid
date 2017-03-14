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
import app.presentation.foundation.presenter.Presenter
import app.presentation.foundation.presenter.ViewPresenter
import app.presentation.foundation.transformations.Transformations
import app.presentation.foundation.widgets.Notifications
import app.presentation.sections.users.UsersWireframe
import io.reactivex.functions.Consumer
import javax.inject.Inject

class UserPresenter @Inject constructor(transformations: Transformations, private val wireframe: UsersWireframe,
                                                 notifications: Notifications) : Presenter<UserPresenter.View>(transformations, notifications) {

    override fun onBindView(view: View) {
        super.onBindView(view)

        wireframe.userScreen
                .compose(transformations.safely())
                .compose(transformations.loading())
                .compose(transformations.reportOnSnackBar())
                .subscribe(Consumer { view.showUser(it) })
    }

    interface View : ViewPresenter {
        fun showUser(user: User)
    }
}
