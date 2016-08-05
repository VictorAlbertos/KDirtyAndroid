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
import org.base_app_android.R;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rx.Observable;
import rx.exceptions.CompositeException;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public final class ExceptionFormatterTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  private static final String GENERIC_ERROR = "error",
      SPECIFIC_ERROR = "specific_error";
  @Mock Resources resources;
  private ExceptionFormatter exceptionFormatterUT;

  @Before public void init() {
    exceptionFormatterUT = Mockito.spy(new ExceptionFormatter(resources));
    when(resources.getString(R.string.errors_happen)).thenReturn(GENERIC_ERROR);
  }

  @Test
  public void When_Build_Is_Production_And_Exception_Is_Not_Networking_Then_Show_Common_Error() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false);
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Observable<String> oMessage = exceptionFormatterUT.format(new RuntimeException());
    oMessage.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    assertEquals(GENERIC_ERROR, subscriber.getOnNextEvents().get(0));
  }

  @Test public void When_Build_Is_Production_And_Exception_Is_Networking_Then_Show_Its_Error() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false);
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Observable<String> oMessage =
        exceptionFormatterUT.format(new NetworkResponse.NetworkException(SPECIFIC_ERROR));
    oMessage.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    assertEquals(SPECIFIC_ERROR, subscriber.getOnNextEvents().get(0));
  }

  @Test public void When_Build_Is_Debug_And_Exception_Is_Not_Networking_Then_Show_Its_Error() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true);
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Observable<String> oMessage = exceptionFormatterUT.format(new RuntimeException(SPECIFIC_ERROR));
    oMessage.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    assertEquals(SPECIFIC_ERROR, subscriber.getOnNextEvents().get(0));
  }

  @Test public void When_Build_Is_Debug_And_Exception_Is_Networking_Then_Show_Its_Error() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true);
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Observable<String> oMessage =
        exceptionFormatterUT.format(new NetworkResponse.NetworkException(SPECIFIC_ERROR));
    oMessage.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    assertEquals(SPECIFIC_ERROR, subscriber.getOnNextEvents().get(0));
  }

  @Test public void When_Build_Is_Production_And_Exception_Has_Cause_Do_Not_Show_It() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false);
    TestSubscriber<String> subscriber = new TestSubscriber<>();

    NetworkResponse.NetworkException networkException =
        Mockito.spy(new NetworkResponse.NetworkException(SPECIFIC_ERROR));
    when(networkException.getCause()).thenReturn(new NetworkResponse.NetworkException("CAUSE"));

    Observable<String> oMessage = exceptionFormatterUT.format(networkException);
    oMessage.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    assertEquals(SPECIFIC_ERROR, subscriber.getOnNextEvents().get(0));
  }

  @Test public void When_Build_Is_Debug_And_Exception_Has_Cause_Show_It() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true);
    TestSubscriber<String> subscriber = new TestSubscriber<>();

    NetworkResponse.NetworkException networkException =
        Mockito.spy(new NetworkResponse.NetworkException(SPECIFIC_ERROR));
    when(networkException.getCause()).thenReturn(new NetworkResponse.NetworkException("CAUSE"));

    Observable<String> oMessage = exceptionFormatterUT.format(networkException);
    oMessage.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    assertEquals("specific_error\nCAUSE",
        subscriber.getOnNextEvents().get(0));
  }

  @Test public void When_Build_Is_Release_And_Exception_Is_Composite_Do_Not_Show_It() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(false);
    TestSubscriber<String> subscriber = new TestSubscriber<>();

    CompositeException exception =
        new CompositeException(new RuntimeException("1"), new RuntimeException("2"));

    Observable<String> oMessage = exceptionFormatterUT.format(exception);
    oMessage.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    assertEquals(GENERIC_ERROR, subscriber.getOnNextEvents().get(0));
  }

  @Test public void When_Build_Is_Debug_And_Exception_Is_Composite_Do_Not_Show_It() {
    when(exceptionFormatterUT.isBuildConfigDebug()).thenReturn(true);
    TestSubscriber<String> subscriber = new TestSubscriber<>();

    CompositeException exception =
        new CompositeException(new RuntimeException("1"), new NetworkResponse.NetworkException("2"));

    Observable<String> oMessage = exceptionFormatterUT.format(exception);
    oMessage.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    assertEquals("2 exceptions occurred. \n"
            + "Chain of Causes for CompositeException In Order Received =>\n"
            + "RuntimeException -> 1\n"
            + "NetworkException -> 2\n",
        subscriber.getOnNextEvents().get(0));
  }
}