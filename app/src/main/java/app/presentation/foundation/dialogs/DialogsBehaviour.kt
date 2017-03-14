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

package app.presentation.foundation.dialogs

import android.support.v4.content.ContextCompat
import app.presentation.foundation.BaseApp
import com.afollestad.materialdialogs.MaterialDialog
import org.base_app_android.R
import javax.inject.Inject

class DialogsBehaviour @Inject constructor(private val baseApp: BaseApp) : Dialogs {
    private var materialDialog: MaterialDialog? = null

    override fun showLoading() {
        if (materialDialog == null) {
            materialDialog = builderLoading(null).show()
        }
    }

    override fun showLoading(content: String) {
        if (materialDialog == null) {
            materialDialog = builderLoading(content).show()
        }
    }

    override fun showNoCancelableLoading() {
        if (materialDialog == null) {
            materialDialog = builderLoading(null)
                    .cancelable(false)
                    .show()
        }
    }

    override fun showNoCancelableLoading(content: String) {
        if (materialDialog == null) {
            materialDialog = builderLoading(content)
                    .cancelable(false)
                    .show()
        }
    }

    override fun hideLoading() {
        if (materialDialog != null) {
            materialDialog!!.dismiss()
            materialDialog = null
        }
    }

    private fun builderLoading(content: String?): MaterialDialog.Builder {
        return MaterialDialog.Builder(baseApp.liveActivity!!)
                .titleColorRes(R.color.colorPrimaryDark)
                .contentColor(ContextCompat.getColor(baseApp, R.color.colorPrimaryDark))
                .widgetColorRes(R.color.colorPrimaryDark)
                .title(baseApp.getString(R.string.app_name))
                .content(content ?: baseApp.getString(R.string.loading))
                .progress(true, 0)
    }
}
