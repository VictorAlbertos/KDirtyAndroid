/*
 * Copyright 2016 Victor Albertos
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

package app.presentation.foundation.transformations;

import app.data.foundation.Resources;
import app.data.foundation.net.NetworkResponse;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.observers.TestObserver;
import org.base_app_android.R;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import timber.log.Timber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public final class ExceptionFormatterTest {
  private static final String LINE_SEPARATOR = "line.separator";
  
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  private static final String GENERIC_ERROR = "error",
      SPECIFIC_ERROR = "specific_error";
  @Mock Resources resources;
  private ExceptionFormatter exceptionFormatterUT;

  @Before public void init() {
    exceptionFormatterUT = Mockito.spy(new ExceptionFormatter(resources, new Timber.Tree() {
      @Override protected void log(int priority, String tag, String message, Throwable t) {
        // This method is intentionally empty to provide a dummy implementation for Timber.
      }
    }));
    when(resources.getString(R.string.errors_happen)).thenReturn(GENERIC_ERROR);
  }

  @Test
  public void When_Build_Is_Production_And_Exception_Is_Not_Networking_Then_Show_Common_Error() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false);
    TestObserver<String> observer = exceptionFormatterUT.format(new RuntimeException()).test();
    observer.awaitTerminalEvent();

    observer.assertNoErrors();
    observer.assertValueCount(1);
    assertEquals(GENERIC_ERROR, observer.values().get(0));
  }

  @Test public void When_Build_Is_Production_And_Exception_Is_Networking_Then_Show_Its_Error() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false);
    TestObserver<String> observer =
        exceptionFormatterUT.format(new NetworkResponse.NetworkException(SPECIFIC_ERROR)).test();
    observer.awaitTerminalEvent();

    observer.assertNoErrors();
    observer.assertValueCount(1);
    assertEquals(SPECIFIC_ERROR, observer.values().get(0));
  }

  @Test public void When_Build_Is_Debug_And_Exception_Is_Not_Networking_Then_Show_Its_Error() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true);
    TestObserver<String> observer =
        exceptionFormatterUT.format(new RuntimeException(SPECIFIC_ERROR)).test();
    observer.awaitTerminalEvent();

    observer.assertNoErrors();
    observer.assertValueCount(1);
    assertEquals(SPECIFIC_ERROR, observer.values().get(0));
  }

  @Test public void When_Build_Is_Debug_And_Exception_Is_Networking_Then_Show_Its_Error() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true);
    TestObserver<String> observer =
        exceptionFormatterUT.format(new NetworkResponse.NetworkException(SPECIFIC_ERROR)).test();
    observer.awaitTerminalEvent();

    observer.assertNoErrors();
    observer.assertValueCount(1);
    assertEquals(SPECIFIC_ERROR, observer.values().get(0));
  }

  @Test public void When_Build_Is_Production_And_Exception_Has_Cause_Do_Not_Show_It() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false);

    NetworkResponse.NetworkException networkException =
        Mockito.spy(new NetworkResponse.NetworkException(SPECIFIC_ERROR));
    when(networkException.getCause()).thenReturn(new NetworkResponse.NetworkException("CAUSE"));

    TestObserver<String> observer = exceptionFormatterUT.format(networkException).test();
    observer.awaitTerminalEvent();

    observer.assertNoErrors();
    observer.assertValueCount(1);
    assertEquals(SPECIFIC_ERROR, observer.values().get(0));
  }

  @Test public void When_Build_Is_Debug_And_Exception_Has_Cause_Show_It() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true);

    NetworkResponse.NetworkException networkException =
        Mockito.spy(new NetworkResponse.NetworkException(SPECIFIC_ERROR));
    when(networkException.getCause()).thenReturn(new NetworkResponse.NetworkException("CAUSE"));

    TestObserver<String> observer = exceptionFormatterUT.format(networkException).test();
    observer.awaitTerminalEvent();

    observer.assertNoErrors();
    observer.assertValueCount(1);
    assertEquals("specific_error" +  System.getProperty(LINE_SEPARATOR) + "CAUSE",
        observer.values().get(0));
  }

  @Test public void When_Build_Is_Release_And_Exception_Is_Composite_Do_Not_Show_It() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false);

    CompositeException exception =
        new CompositeException(new RuntimeException("1"), new RuntimeException("2"));

    TestObserver<String> observer = exceptionFormatterUT.format(exception).test();
    observer.awaitTerminalEvent();

    observer.assertNoErrors();
    observer.assertValueCount(1);
    assertEquals(GENERIC_ERROR, observer.values().get(0));
  }

  @Test public void When_Build_Is_Debug_And_Exception_Is_Composite_Do_Not_Show_It() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true);

    CompositeException exception =
        new CompositeException(new RuntimeException("1"),
            new NetworkResponse.NetworkException("2"));

    TestObserver<String> observer = exceptionFormatterUT.format(exception).test();
    observer.awaitTerminalEvent();

    observer.assertNoErrors();
    observer.assertValueCount(1);
    assertEquals("2 exceptions occurred. " +  System.getProperty(LINE_SEPARATOR)
            + "Chain of Causes for CompositeException In Order Received =>" +  System.getProperty(LINE_SEPARATOR)
            + "RuntimeException -> 1" +  System.getProperty(LINE_SEPARATOR)
            + "NetworkException -> 2" +  System.getProperty(LINE_SEPARATOR),
        observer.values().get(0));
  }
}