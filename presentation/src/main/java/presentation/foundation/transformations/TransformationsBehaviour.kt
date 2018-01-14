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

import presentation.foundation.widgets.Dialogs
import presentation.foundation.widgets.Notifications
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleTransformer
import javax.inject.Inject
import javax.inject.Named

class TransformationsBehaviour @Inject constructor(private val exceptionFormatter: ExceptionFormatter,
                                          private val notifications: Notifications,
                                          private val dialogs: Dialogs,
                                          private @Named("mainThread") val mainThread: Scheduler,
                                          private @Named("backgroundThread") val backgroundThread: Scheduler) : Transformations {

    /**
     * Prepare the single to use an io thread for subscription and to observe on the UI thread only after
     * the stream of data has reached this point.
     */
    override fun <T> schedules(): SingleTransformer<T, T> =
            SingleTransformer {
                it.subscribeOn(backgroundThread)
                        .observeOn(mainThread)
            }

    /**
     * Outputs the formatted exception on [android.support.design.widget.Snackbar] and resume
     * the error returning an empty single.
     */
    override fun <T> reportOnSnackBar(): SingleTransformer<T, T> =
            SingleTransformer {
                it.doOnError { notifications.showSnackBar(exceptionFormatter.format(it)) }
                        .onErrorResumeNext { Single.never() }
            }

    /**
     * Outputs the formatted exception on [android.widget.Toast] and resume the error returning
     * an empty single.
     */
    override fun <T> reportOnToast(): SingleTransformer<T, T> =
            SingleTransformer {
                it.doOnError { notifications.showToast(exceptionFormatter.format(it)) }
                        .onErrorResumeNext { Single.never() }
            }

    /**
     * Show a loading dialog just before single is subscribed and hide it after it is completed.
     */
    override fun <T> loading(): SingleTransformer<T, T> =
            SingleTransformer {
                it.doOnSubscribe { dialogs.showLoading() }
                        .doOnSuccess { dialogs.hideLoading() }
                        .doOnError { dialogs.hideLoading() }
            }

    /**
     * Show a loading dialog just before single is subscribed and hide it after it is completed.

     * @param content Text to display in the dialog
     */
    override fun <T> loading(content: String): SingleTransformer<T, T> =
            SingleTransformer {
                it.doOnSubscribe { dialogs.showNoCancelableLoading(content) }
                        .doOnSuccess { dialogs.hideLoading() }
                        .doOnError { dialogs.hideLoading() }
            }
}
