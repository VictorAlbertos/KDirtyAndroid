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

package app.presentation.sections.users

import android.content.Intent
import app.data.foundation.WireframeRepository
import app.data.sections.users.User
import app.presentation.foundation.BaseApp
import app.presentation.sections.users.detail.UserActivity
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UsersWireframe @Inject constructor(private val baseApp: BaseApp,
                                         private val wireframeRepository: WireframeRepository) {

    fun userScreen(user: User): Completable {
        return wireframeRepository
                .put(UserActivity::class.java.name, user)
                .doOnComplete {
                    baseApp.liveActivity!!
                            .startActivity(Intent(baseApp, UserActivity::class.java))
                }
    }

    val userScreen: Single<User>
        get() = wireframeRepository
                .get<User>(UserActivity::class.java.name)
}
