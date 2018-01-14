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

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import presentation.foundation.widgets.Dialogs
import presentation.foundation.widgets.Notifications

class TransformationsBehaviourTest {
    val FORMATTED_ERROR = "formatted_error"
    val SUCCESS_MESSAGE = "success_message"
    val exceptionFormatter: ExceptionFormatter = mock()
    val notifications: Notifications = mock()
    val dialogs: Dialogs = mock()
    lateinit var transformationsBehaviourUT: TransformationsBehaviour

    @Before fun before() {
        whenever(exceptionFormatter.format(any()))
                .thenReturn(Single.just(FORMATTED_ERROR))

        transformationsBehaviourUT = TransformationsBehaviour(exceptionFormatter,
                notifications, dialogs, Schedulers.io(), Schedulers.io())
    }

    @Test fun Verify_Safely_Success() {
        val observer = Single.just(SUCCESS_MESSAGE)
                .compose(transformationsBehaviourUT.schedules<String>())
                .test()

        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == SUCCESS_MESSAGE }
    }

    @Test fun Verify_Safely_Error() {
        val observer = Single.error<String>(RuntimeException())
                .compose(transformationsBehaviourUT.schedules<String>())
                .test()

        observer.awaitTerminalEvent()
        observer.assertError(RuntimeException::class.java)
                .assertNoValues()
    }

    @Test fun Verify_Safely_CancellationException() {
        val observer = Single.just(SUCCESS_MESSAGE)
                .compose(transformationsBehaviourUT.schedules<String>())
                .test()

        observer.assertNoValues()
                .assertNoErrors()
    }

    @Test fun Verify_ReportOnSnackBar_Success() {
        val observer = Single.just(SUCCESS_MESSAGE)
                .compose(transformationsBehaviourUT.reportOnSnackBar<String>())
                .test()

        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == SUCCESS_MESSAGE }

        verify(exceptionFormatter, never()).format(any())
        verify(notifications, never()).showSnackBar(any<Single<String>>())
    }

    @Test fun Verify_ReportOnSnackBar_Error() {
        val observer = Single.error<String>(RuntimeException())
                .compose(transformationsBehaviourUT.reportOnSnackBar<String>())
                .test()

        observer.assertNoErrors()
                .assertNoValues()
        verify(exceptionFormatter).format(any())
        verify(notifications).showSnackBar(any<Single<String>>())
    }

    @Test fun Verify_ReportOnToast_Success() {
        val observer = Single.just(SUCCESS_MESSAGE)
                .compose(transformationsBehaviourUT.reportOnToast<String>())
                .test()
        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == SUCCESS_MESSAGE }
        verify(exceptionFormatter, never()).format(any())
        verify(notifications, never()).showToast(any<Single<String>>())
    }

    @Test fun Verify_ReportOnToast_Error() {
        val observer = Single.error<String>(RuntimeException())
                .compose(transformationsBehaviourUT.reportOnToast<String>())
                .test()

        observer.assertNoErrors()
                .assertNoValues()
        verify(exceptionFormatter).format(any())
        verify(notifications).showToast(any<Single<String>>())
    }

    @Test fun Verify_Loading() {
        val observer = Single.just(SUCCESS_MESSAGE)
                .compose(transformationsBehaviourUT.loading<String>())
                .test()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == SUCCESS_MESSAGE }
    }

}
