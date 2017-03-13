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

package app.data.foundation

import io.reactivecache2.ProviderGroup
import io.reactivecache2.ReactiveCache
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * A repository to persist on both memory and disk layer the data shared between screens. It's
 * intended to be used by specifics wireframes.
 */
class WireframeRepository @Inject constructor(reactiveCache: ReactiveCache) {
    private val cacheProvider: ProviderGroup<Any> =
            reactiveCache.providerGroup<Any>().withKey<ProviderGroup<Any>>("wireframe")

    /**
     * Given a key, return an observable containing the previously cached data.
     * @param key the key with the associated object.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): Single<T> =
            cacheProvider.read(key)
                    .onErrorResumeNext {
                        val message = "There is not cached data in wireframe repository for key $key"
                        Single.error(RuntimeException(message, it))
                    } as Single<T>

    /**
     * Given a key, cache the associated data.
     * @param key the key with the associated object.
     * @param data the data to be cached.
     */
    fun put(key: String, data: Any): Completable =
            Single.just(data)
                    .compose(cacheProvider.replace(key))
                    .toCompletable()
}
