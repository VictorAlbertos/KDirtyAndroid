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

package presentation.sections.users

import android.content.Intent
import data.sections.users.User
import io.reactivex.Completable
import io.victoralbertos.jolyglot.JolyglotGenerics
import presentation.foundation.BaseApp
import presentation.foundation.helpers.extensions.get
import presentation.foundation.helpers.extensions.put
import presentation.sections.users.detail.UserActivity
import javax.inject.Inject

class UsersWireframe @Inject constructor(private val baseApp: BaseApp,
                                         private val jolyglot: JolyglotGenerics) {
    private val keyUser = "keyUser"

    fun userScreen(user: User) = Completable.fromAction {
        val intent = Intent(baseApp, UserActivity::class.java)
                .put(keyUser, user, jolyglot)

        baseApp.liveActivity.startActivity(intent)
    }

    fun user(): User = baseApp.liveActivity.intent.get(keyUser, jolyglot)
}
