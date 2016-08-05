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

package app.presentation.foundation.dagger;

import android.os.AsyncTask;
import app.data.foundation.dagger.DataModule;
import app.presentation.foundation.BaseApp;
import app.presentation.foundation.dialogs.Dialogs;
import app.presentation.foundation.dialogs.DialogsBehaviour;
import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.notifications.NotificationsBehaviour;
import app.presentation.foundation.transformations.Transformations;
import app.presentation.foundation.transformations.TransformationsBehaviour;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module(includes = DataModule.class) public class PresentationModule {
  private final BaseApp baseApp;

  public PresentationModule(BaseApp baseApp) {
    this.baseApp = baseApp;
  }

  @Provides BaseApp provideApplication() {
    return baseApp;
  }

  @Provides Transformations provideTransformations(TransformationsBehaviour
      transformationsBehaviour) {
    return transformationsBehaviour;
  }

  @Provides Notifications provideNotifications(NotificationsBehaviour notificationsBehaviour) {
    return notificationsBehaviour;
  }

  @Provides Dialogs provideDialogs(DialogsBehaviour dialogsBehaviour) {
    return dialogsBehaviour;
  }

  /**
   * Sync with main thread after subscribing to observables emitting from data layer.
   */
  @Named("mainThread")
  @Provides Scheduler provideSchedulerMainThread() {
    return AndroidSchedulers.mainThread();
  }

  /**
   * Using this executor as the scheduler for all async operations allow us to tell espresso when
   * the app is in an idle state in order to wait for the response.
   */
  @Named("backgroundThread")
  @Provides Scheduler provideSchedulerBackgroundThread() {
    return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
  }
}
