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

package app.presentation.foundation.dialogs;

import android.support.v4.content.ContextCompat;
import app.presentation.foundation.BaseApp;
import com.afollestad.materialdialogs.MaterialDialog;
import javax.inject.Inject;
import org.base_app_android.R;

public final class DialogsBehaviour implements Dialogs {
  private final BaseApp baseApp;
  private MaterialDialog materialDialog;

  @Inject public DialogsBehaviour(BaseApp baseApp) {
    this.baseApp = baseApp;
  }

  /**
   * {inherit docs}
   */
  @Override public void showLoading() {
    materialDialog = builderLoading()
        .show();
  }

  /**
   * {inherit docs}
   */
  @Override public void showNoCancelableLoading() {
    materialDialog = builderLoading()
        .cancelable(false)
        .show();
  }

  /**
   * {inherit docs}
   */
  @Override public void hideLoading() {
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }
  }

  private MaterialDialog.Builder builderLoading() {
    return new MaterialDialog.Builder(baseApp.getLiveActivity())
        .titleColorRes(R.color.colorPrimaryDark)
        .contentColor(ContextCompat.getColor(baseApp, R.color.colorPrimaryDark))
        .widgetColorRes(R.color.colorPrimaryDark)
        .title(baseApp.getString(R.string.app_name))
        .content(baseApp.getString(R.string.loading))
        .progress(true, 0);
  }
}
