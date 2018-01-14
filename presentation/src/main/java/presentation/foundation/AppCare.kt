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

package presentation.foundation

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Track the current activity using the LifecycleCallback application.
 */
object AppCare {
    fun getLiveActivity(): Activity? = liveActivity ?: lastKnownActivity

    private var liveActivity: Activity? = null
    private var lastKnownActivity: Activity? = null

    fun registerActivityLifeCycle(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                liveActivity = activity
            }

            override fun onActivityStarted(activity: Activity?) {
                // Do nothing.  Exists to satisfy ActivityLifecycleCallbacks interface.
            }

            override fun onActivityResumed(activity: Activity?) {
                liveActivity = activity
            }

            override fun onActivityPaused(activity: Activity?) {
                liveActivity = null
            }

            override fun onActivityStopped(activity: Activity?) {
                // Do nothing.  Exists to satisfy ActivityLifecycleCallbacks interface.
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                lastKnownActivity = activity
            }

            override fun onActivityDestroyed(activity: Activity?) {
                lastKnownActivity = null
            }
        })
    }
}
