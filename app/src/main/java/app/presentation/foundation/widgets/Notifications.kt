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

package app.presentation.foundation.widgets

import android.R
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.widget.Toast
import app.presentation.foundation.BaseApp
import io.reactivex.Single
import javax.inject.Inject

class Notifications @Inject constructor(private val baseApp: BaseApp) {

    /**
     * For output toast messages from the data layer.
     */
    fun showToast(sTitle: Single<String>) {
        sTitle.subscribe { title -> Toast.makeText(baseApp, title, Toast.LENGTH_LONG).show() }
    }

    /**
     * For output output snackbar messages from the data layer.
     */
    fun showSnackBar(sTitle: Single<String>) {
        val activity = baseApp.liveActivity
        if (activity != null) {
            sTitle.subscribe { title ->
                Snackbar.make(activity.findViewById(R.id.content),
                        title, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /**
     * For output output toast messages from the presentation layer.
     */
    fun showToast(@StringRes idString: Int) {
        Toast.makeText(baseApp, baseApp.getString(idString),
                Toast.LENGTH_LONG).show()
    }

    /**
     * For output snackbar messages from the presentation layer.
     */
    fun showSnackBar(@StringRes idString: Int) {
        val activity = baseApp.liveActivity
        if (activity != null) {
            Snackbar.make(activity.findViewById(R.id.content),
                    baseApp.getString(idString), Snackbar.LENGTH_LONG).show()
        }
    }
}
