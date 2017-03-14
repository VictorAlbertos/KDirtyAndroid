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

package app.data.sections.users

import io.victoralbertos.mockery.api.built_in_mockery.DTOArgs
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`

class UserDTO : DTOArgs.Behaviour<User> {
    override fun validate(candidate: User) {
        MatcherAssert.assertThat(candidate, notNullValue())
        MatcherAssert.assertThat(candidate.id, `is`<Int>(not(0)))
        MatcherAssert.assertThat(candidate.login.isEmpty(), `is`(not(true)))
    }

    override fun legal(args: Array<out Any>): User {
        val userName = args[0] as String
        val user = User(1, userName, "https://cdn0.iconfinder.com/data/icons/octicons/1024/mark-github-256.png")
        return user
    }
}

class UsersDTO : DTOArgs.Behaviour<List<User>> {
    override fun validate(candidate: List<User>) {
        MatcherAssert.assertThat(candidate, notNullValue())
        MatcherAssert.assertThat(candidate.isEmpty(), `is`(not(true)))

        val (id, login) = candidate[0]
        MatcherAssert.assertThat(id, `is`(not(0)))
        MatcherAssert.assertThat(login.isEmpty(), `is`(not(true)))
    }

    override fun legal(args: Array<out Any>): List<User> {
        val lastIdQueried = args[0] as Int + 1
        var perPage = args[1] as Int
        perPage = if (perPage == 0) 30 else perPage

        return (lastIdQueried..lastIdQueried + perPage).map {
            User(it, "User " + it, "https://cdn0.iconfinder.com/data/icons/octicons/1024/mark-github-256.png")
        }
    }
}

