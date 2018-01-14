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

package data.sections.users

import data.foundation.PaginatedItems
import data.foundation.net.NetworkResponse
import io.reactivecache2.ProviderGroup
import io.reactivecache2.ReactiveCache
import io.reactivex.Single
import io.rx_cache2.Reply
import javax.inject.Inject

class UserRepository @Inject constructor(private val githubUsersApi: GithubUsersApi,
                                         private val networkResponse: NetworkResponse,
                                         reactiveCache: ReactiveCache) {
    private val FIRST_DEFAULT_ID = 0;
    private val USERS_PER_PAGE = 50
    private val cacheProvider = reactiveCache.providerGroup<List<User>>()
            .withKey<ProviderGroup<List<User>>>("users")

    fun getUsers(lastIdQueried: Int?, refresh: Boolean): Single<PaginatedItems<User>> =
            getUsersReply(lastIdQueried, refresh).map { it.data }
                    .map { PaginatedItems(it.last().id, it, it.isNotEmpty()) }

    fun getUsersReply(lastIdQueried: Int?, refresh: Boolean): Single<Reply<List<User>>> {
        val id = lastIdQueried ?: FIRST_DEFAULT_ID
        val apiCall: Single<List<User>> = githubUsersApi
                .getUsers(id, USERS_PER_PAGE)
                .compose(networkResponse.process())

        return if (refresh) {
            apiCall.compose(cacheProvider.replaceAsReply(id))
        } else {
            apiCall.compose(cacheProvider.readWithLoaderAsReply(id))
        }
    }

    fun getRecentUser(): Single<User> = getUsersReply(FIRST_DEFAULT_ID, false).map { it.data[0] }

    fun searchByUserName(username: String): Single<User> =
            githubUsersApi.getUserByName(username).compose(networkResponse.process())


}