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

import app.presentation.foundation.widgets.Dialogs;
import app.presentation.foundation.widgets.Notifications;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import java.util.concurrent.CancellationException;
import javax.inject.Inject;
import javax.inject.Named;

public final class TransformationsBehaviour implements Transformations {
  private SingleTransformer lifecycle;
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

  public void setLifecycle(SingleTransformer lifecycle) {
    this.lifecycle = lifecycle;
  }

  public <T> SingleTransformer<T, T> safely() {
    return single -> single
        .subscribeOn(backgroundThread)
        .<T>observeOn(mainThread)
        .<T>compose(lifecycle)
        .<T>onErrorResumeNext(error -> {
          if (error instanceof CancellationException) return Single.never();
          return Single.error((Throwable) error);
        });
  }

  public <T> SingleTransformer<T, T> reportOnSnackBar() {
    return single -> single
        .<T>doOnError(throwable -> {
          Single<String> formattedError = exceptionFormatter.format(throwable);
          notifications.showSnackBar(formattedError);
        })
        .<T>onErrorResumeNext(error -> Single.never());
  }

  public <T> SingleTransformer<T, T> reportOnToast() {
    return single -> single
        .<T>doOnError(throwable -> {
          Single<String> formattedError = exceptionFormatter.format(throwable);
          notifications.showToast(formattedError);
        })
        .<T>onErrorResumeNext(throwable -> Single.never());
  }

  public <T> SingleTransformer<T, T> loading() {
    return single -> single
        .doOnSubscribe(disposable -> dialogs.showLoading())
        .doOnSuccess(_I -> dialogs.hideLoading())
        .doOnError(throwable -> dialogs.hideLoading());
  }

  public <T> SingleTransformer<T, T> loading(String content) {
    return single -> single
        .doOnSubscribe(disposable -> dialogs.showNoCancelableLoading(content))
        .doOnSuccess(_I -> dialogs.hideLoading())
        .doOnError(throwable -> dialogs.hideLoading());
  }
}
