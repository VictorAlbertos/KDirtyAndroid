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

import app.presentation.foundation.widgets.Notifications;
import app.presentation.foundation.transformations.Transformations;
import io.reactivex.SingleTransformer;
import javax.inject.Inject;

/**
 * Base class for every new presenter.
 *
 * @param <V> the associated view abstraction with this presenter.
 */
public class Presenter<V extends ViewPresenter> {
  protected V view;
  protected final Transformations transformations;
  protected final Notifications notifications;

  @Inject public Presenter(Transformations transformations, Notifications notifications) {
    this.transformations = transformations;
    this.notifications = notifications;
  }

  /**
   * Called from BaseFragment or BaseActivity to be able to sync single with their lifecycle.
   *
   * @param lifeCycle the Transformer supplied by RxLifecycle for both Fragment and Activity.
   */
  public void bindLifeCycle(SingleTransformer lifeCycle) {
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
}
