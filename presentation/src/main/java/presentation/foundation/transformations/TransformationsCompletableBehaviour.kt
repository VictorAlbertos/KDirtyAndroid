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
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class TransformationsCompletableBehaviour @Inject constructor(private val exceptionFormatter: ExceptionFormatter,
                                                              private val notifications: Notifications,
                                                              private val dialogs: Dialogs,
                                                              private @Named("mainThread") val mainThread: Scheduler,
                                                              private @Named("backgroundThread") val backgroundThread: Scheduler) : TransformationsCompletable {

    /**
     * Prepare the single to use an io thread for subscription and to observe on the UI thread only after
     * the stream of data has reached this point.
     */
    override fun schedules(): CompletableTransformer =
            CompletableTransformer {
                it.subscribeOn(backgroundThread)
                        .observeOn(mainThread)
            }

    /**
     * Outputs the formatted exception on [android.support.design.widget.Snackbar] and resume
     * the error returning an empty single.
     */
    override fun reportOnSnackBar(): CompletableTransformer =
            CompletableTransformer {
                it.doOnError { notifications.showSnackBar(exceptionFormatter.format(it)) }
                        .onErrorResumeNext { Completable.never() }
            }

    /**
     * Outputs the formatted exception on [android.widget.Toast] and resume the error returning
     * an empty single.
     */
    override fun reportOnToast(): CompletableTransformer =
            CompletableTransformer {
                it.doOnError { notifications.showToast(exceptionFormatter.format(it)) }
                        .onErrorResumeNext { Completable.never() }
            }

    /**
     * Show a loading dialog just before single is subscribed and hide it after it is completed.
     */
    override fun loading(): CompletableTransformer =
            CompletableTransformer {
                it.doOnSubscribe { dialogs.showLoading() }
                        .doOnComplete { dialogs.hideLoading() }
                        .doOnError { dialogs.hideLoading() }
            }

    /**
     * Show a loading dialog just before single is subscribed and hide it after it is completed.

     * @param content Text to display in the dialog
     */
    override fun loading(content: String): CompletableTransformer =
            CompletableTransformer {
                it.doOnSubscribe { dialogs.showNoCancelableLoading(content) }
                        .doOnComplete() { dialogs.hideLoading() }
                        .doOnError { dialogs.hideLoading() }
            }
}
