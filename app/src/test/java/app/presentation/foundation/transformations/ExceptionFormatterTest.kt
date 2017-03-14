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

package app.presentation.foundation.transformations

import app.data.foundation.Resources
import app.data.foundation.net.NetworkException
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.exceptions.CompositeException
import junit.framework.TestCase.assertEquals
import org.base_app_android.R
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import timber.log.Timber

class ExceptionFormatterTest {
    val LINE_SEPARATOR = "line.separator"
    val GENERIC_ERROR = "error"
    val SPECIFIC_ERROR = "specific_error"
    val resources: Resources = mock()
    lateinit var exceptionFormatterUT: ExceptionFormatter

    @Before fun before() {
        exceptionFormatterUT = spy(ExceptionFormatter(resources, object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
                // This method is intentionally empty to provide a dummy implementation for Timber.
            }
        }))
        whenever(resources.getString(R.string.errors_happen)).thenReturn(GENERIC_ERROR)
    }

    @Test
    fun When_Build_Is_Production_And_Exception_Is_Not_Networking_Then_Show_Common_Error() {
        whenever(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false)
        val observer = exceptionFormatterUT.format(RuntimeException()).test()
        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == GENERIC_ERROR }
    }

    @Test fun When_Build_Is_Production_And_Exception_Is_Networking_Then_Show_Its_Error() {
        whenever(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false)
        val observer = exceptionFormatterUT.format(NetworkException(SPECIFIC_ERROR)).test()
        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == SPECIFIC_ERROR }
    }

    @Test fun When_Build_Is_Debug_And_Exception_Is_Not_Networking_Then_Show_Its_Error() {
        whenever(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true)
        val observer = exceptionFormatterUT.format(RuntimeException(SPECIFIC_ERROR)).test()
        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == SPECIFIC_ERROR }
    }

    @Test fun When_Build_Is_Debug_And_Exception_Is_Networking_Then_Show_Its_Error() {
        whenever(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true)
        val observer = exceptionFormatterUT.format(NetworkException(SPECIFIC_ERROR)).test()
        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == SPECIFIC_ERROR }
    }

    @Test fun When_Build_Is_Production_And_Exception_Has_Cause_Do_Not_Show_It() {
        whenever(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false)

        val networkException = Mockito.spy(NetworkException(SPECIFIC_ERROR))
        whenever(networkException.cause).thenReturn(NetworkException("CAUSE"))

        val observer = exceptionFormatterUT.format(networkException).test()
        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == SPECIFIC_ERROR }
    }

    @Test fun When_Build_Is_Debug_And_Exception_Has_Cause_Show_It() {
        whenever(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true)

        val networkException = Mockito.spy(NetworkException(SPECIFIC_ERROR))
        whenever(networkException.cause).thenReturn(NetworkException("CAUSE"))

        val observer = exceptionFormatterUT.format(networkException).test()
        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == SPECIFIC_ERROR + System.getProperty(LINE_SEPARATOR) + "CAUSE" }
    }

    @Test fun When_Build_Is_Release_And_Exception_Is_Composite_Do_Not_Show_It() {
        whenever(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false)

        val exception = CompositeException(RuntimeException("1"), RuntimeException("2"))

        val observer = exceptionFormatterUT.format(exception).test()
        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
                .assertValue { it == GENERIC_ERROR }
    }

    @Test fun When_Build_Is_Debug_And_Exception_Is_Composite_Do_Not_Show_It() {
        whenever(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true)

        val exception = CompositeException(RuntimeException("1"),
                NetworkException("2"))

        val observer = exceptionFormatterUT.format(exception).test()
        observer.awaitTerminalEvent()

        observer.assertNoErrors()
                .assertValueCount(1)
        assertEquals("2 exceptions occurred. " + System.getProperty(LINE_SEPARATOR)
                + "Chain of Causes for CompositeException In Order Received =>" + System.getProperty(LINE_SEPARATOR)
                + "RuntimeException -> 1" + System.getProperty(LINE_SEPARATOR)
                + "NetworkException -> 2" + System.getProperty(LINE_SEPARATOR),
                observer.values()[0])
    }
}

