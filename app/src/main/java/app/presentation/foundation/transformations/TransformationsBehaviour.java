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
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import rx.Scheduler;

public final class TransformationsBehaviour implements Transformations {
  private Observable.Transformer lifecycle;
  private final ExceptionFormatter exceptionFormatter;
  private final Notifications notifications;
  private final Dialogs dialogs;
  private final Scheduler mainThread, backgroundThread;

  @Inject public TransformationsBehaviour(ExceptionFormatter exceptionFormatter,
      Notifications notifications, Dialogs dialogs, @Named("mainThread") Scheduler mainThread,
      @Named("backgroundThread") Scheduler backgroundThread) {
    this.exceptionFormatter = exceptionFormatter;
    this.notifications = notifications;
    this.dialogs = dialogs;
    this.mainThread = mainThread;
    this.backgroundThread = backgroundThread;
  }

  public void setLifecycle(Observable.Transformer lifecycle) {
    this.lifecycle = lifecycle;
  }

  /**
   * {inherit docs}
   */
  public <T> Observable.Transformer<T, T> safely() {
    return observable -> observable
        .subscribeOn(backgroundThread)
        .observeOn(mainThread)
        .compose(lifecycle);
  }

  /**
   * {inherit docs}
   */
  public <T> Observable.Transformer<T, T> reportOnSnackBar() {
    return observable -> observable
        .doOnError(throwable -> {
          Observable<String> formattedError = exceptionFormatter.format(throwable);
          notifications.showSnackBar(formattedError);
        })
        .onErrorResumeNext(throwable -> Observable.empty());
  }

  /**
   * {inherit docs}
   */
  public <T> Observable.Transformer<T, T> reportOnToast() {
    return observable -> observable
        .doOnError(throwable -> {
          Observable<String> formattedError = exceptionFormatter.format(throwable);
          notifications.showToast(formattedError);
        })
        .onErrorResumeNext(throwable -> Observable.empty());
  }

  /**
   * {inherit docs}
   */
  public <T> Observable.Transformer<T, T> loading() {
    return observable -> observable
        .doOnSubscribe(dialogs::showLoading)
        .doOnCompleted(dialogs::hideLoading);
  }
}