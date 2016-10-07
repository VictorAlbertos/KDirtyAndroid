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

package app.presentation.foundation.presenter;

import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.transformations.Transformations;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import javax.inject.Inject;
import rx_fcm.Message;
import rx_fcm.internal.RxFcm;

/**
 * Base class for every new presenter.
 *
 * @param <V> the associated view abstraction with this presenter.
 */
public class Presenter<V extends ViewPresenter> implements SyncView.Matcher {
  protected V view;
  protected final Transformations transformations;
  protected final Notifications notifications;
  protected final SyncView syncView;

  @Inject public Presenter(Transformations transformations, Notifications notifications,
      SyncView syncView) {
    this.transformations = transformations;
    this.notifications = notifications;
    this.syncView = syncView;
  }

  /**
   * Called from BaseFragment or BaseActivity to be able to sync observable with their lifecycle.
   *
   * @param lifeCycle the Transformer supplied by RxLifecycle for both Fragment and Activity.
   */
  public void bindLifeCycle(ObservableTransformer lifeCycle) {
    this.transformations.setLifecycle(lifeCycle);
  }

  /**
   * Override this method to run code just after the view has been proper initialized and assiged.
   */
  public void onBindView(V view) {
    this.view = view;
  }

  /**
   * Called when the view is resumed.
   */
  public void onResumeView() {
    //Override if sub-class requires to handle some updates when the view is resumed.
  }

  /**
   * Called when a Fcm notification is received and it matches with the key provided by {@link
   * #matchesTarget(String)}.
   */
  public void onTargetNotification(Observable<Message> oMessage) {
    //Override if sub-class requires to show A Fcm notification.
  }

  /**
   * When a Fcm notification is received and it doesn't match with the key provided by {@link
   * #matchesTarget(String)} a toast is shown and this key is added to syncView as a flag to notify
   * potential screen callers.
   */
  public void onMismatchTargetNotification(Observable<Message> oMessage) {
    notifications.showFcmNotification(
        oMessage.doOnNext(message -> syncView.addScreen(message.target())));
  }

  /**
   * Override if the Presenter requires to be notified, whether by a Fcm notification or due to some
   * other internal event both of them handled by screensSync instance.
   */
  @Override public boolean matchesTarget(String key) {
    return false;
  }

  /**
   * Gets the current Fcm token
   *
   * @return The Fcm token
   */
  protected Observable<String> tokenFcm() {
    return RxFcm.Notifications.currentToken().onErrorResumeNext(throwable -> {
      return Observable.just("");
    });
  }
}
