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

package presentation.foundation.transformations

import android.support.annotation.VisibleForTesting
import data.foundation.Resources
import data.foundation.net.NetworkException
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException
import org.base_app_android.BuildConfig
import org.base_app_android.R
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Format errors to show the most meaningful message depending on the current build variant and the
 * exception type.
 */
class ExceptionFormatter @Inject constructor(val resources: Resources, timberTree: Timber.Tree) {
    init {
        Timber.plant(timberTree)
    }

    fun format(error: Throwable): Single<String> {
        return Single.defer outer@ {
            Timber.e(error)

            if (error is UnknownHostException || error is ConnectException) {
                return@outer Single.just(resources.getString(R.string.connection_error))
            }

            if (!isBuildConfigDebug() && error !is NetworkException) {
                return@outer Single.just(resources.getString(R.string.errors_happen))
            }

            var message = error.message

            if (error.cause != null && isBuildConfigDebug()) {
                message += System.getProperty("line.separator") + error.cause?.message
            }

            if (error is CompositeException) {
                message += System.getProperty("line.separator")

                error.exceptions.forEach {
                    val exceptionName = it.javaClass.simpleName
                    val exceptionMessage = if (it.message == null) "" else it.message
                    message += exceptionName + " -> " + exceptionMessage + System.getProperty("line.separator")
                }
            }

            return@outer Single.just<String>(message)
        }
    }

    @VisibleForTesting
    fun isBuildConfigDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}