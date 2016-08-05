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

package app.presentation.foundation;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Track the current activity using the LifecycleCallback application.
 */
public enum AppCare {
  YesSir;

  private Activity liveActivityOrNull;

  public void takeCareOn(Application application) {
    registerActivityLifeCycle(application);
  }

  private void registerActivityLifeCycle(Application application) {
    application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
      @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        liveActivityOrNull = activity;
      }

      @Override public void onActivityStarted(Activity activity) {
        // Do nothing.  Exists to satisfy ActivityLifecycleCallbacks interface.
      }

      @Override public void onActivityResumed(Activity activity) {
        liveActivityOrNull = activity;
      }

      @Override public void onActivityPaused(Activity activity) {
        liveActivityOrNull = null;
      }

      @Override public void onActivityStopped(Activity activity) {
        // Do nothing.  Exists to satisfy ActivityLifecycleCallbacks interface.
      }

      @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // Do nothing.  Exists to satisfy ActivityLifecycleCallbacks interface.
      }

      @Override public void onActivityDestroyed(Activity activity) {
        // Do nothing.  Exists to satisfy ActivityLifecycleCallbacks interface.
      }
    });
  }

  @Nullable public Activity getLiveActivity() {
    return liveActivityOrNull;
  }
}
