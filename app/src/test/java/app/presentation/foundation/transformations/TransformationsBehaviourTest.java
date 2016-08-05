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

import app.presentation.foundation.dialogs.Dialogs;
import app.presentation.foundation.notifications.Notifications;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class TransformationsBehaviourTest {
  private final static String FORMATTED_ERROR = "formatted_error",
      SUCCESS_MESSAGE = "success_message";
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock ExceptionFormatter exceptionFormatter;
  @Mock Notifications notifications;
  @Mock Dialogs dialogs;
  private TransformationsBehaviour transformationsBehaviourUT;

  @Before public void init() {
    when(exceptionFormatter.format(any()))
        .thenReturn(Observable.just(FORMATTED_ERROR));

    transformationsBehaviourUT = new TransformationsBehaviour(exceptionFormatter,
        notifications, dialogs, Schedulers.io(), Schedulers.io());

    transformationsBehaviourUT.setLifecycle(observable -> observable);
  }

  @Test public void Verify_ReportOnSnackBar_Success() {
     Observable<String> observable = Observable.just(SUCCESS_MESSAGE)
         .compose(transformationsBehaviourUT.reportOnSnackBar());
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    observable.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    verify(exceptionFormatter, never()).format(any());
    verify(notifications, never()).showSnackBar(any(Observable.class));
    assertEquals(SUCCESS_MESSAGE, subscriber.getOnNextEvents().get(0));
  }

  @Test public void Verify_ReportOnSnackBar_Error() {
    Observable<String> observable = Observable.<String>error(new RuntimeException())
        .compose(transformationsBehaviourUT.reportOnSnackBar());
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    observable.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertNoValues();
    verify(exceptionFormatter).format(any());
    verify(notifications).showSnackBar(any(Observable.class));
  }

  @Test public void Verify_ReportOnToast_Success() {
    Observable<String> observable = Observable.just(SUCCESS_MESSAGE)
        .compose(transformationsBehaviourUT.reportOnToast());
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    observable.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    verify(exceptionFormatter, never()).format(any());
    verify(notifications, never()).showToast(any(Observable.class));
    assertEquals(SUCCESS_MESSAGE, subscriber.getOnNextEvents().get(0));
  }

  @Test public void Verify_ReportOnToast_Error() {
    Observable<String> observable = Observable.<String>error(new RuntimeException())
        .compose(transformationsBehaviourUT.reportOnToast());
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    observable.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertNoValues();
    verify(exceptionFormatter).format(any());
    verify(notifications).showToast(any(Observable.class));
  }

  @Test public void Verify_Loading() {
    Observable<String> observable = Observable.just(SUCCESS_MESSAGE)
        .compose(transformationsBehaviourUT.loading());
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    observable.subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    verify(dialogs).showLoading();
    verify(dialogs).hideLoading();

    assertEquals(SUCCESS_MESSAGE, subscriber.getOnNextEvents().get(0));
  }

}