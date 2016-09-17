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

package app.presentation.foundation.notifications;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import app.presentation.foundation.BaseApp;
import io.reactivex.Observable;
import javax.inject.Inject;
import rx_fcm.Message;

public final class NotificationsBehaviour implements Notifications {
  private final BaseApp baseApp;

  @Inject public NotificationsBehaviour(BaseApp baseApp) {
    this.baseApp = baseApp;
  }

  /**
   * {inherit docs}
   */
  @Override public void showToast(Observable<String> oTitle) {
    oTitle.subscribe(title -> Toast.makeText(baseApp, title, Toast.LENGTH_LONG).show());
  }

  /**
   * {inherit docs}
   */
  @Override public void showSnackBar(Observable<String> oTitle) {
    Activity activity = baseApp.getLiveActivity();
    if (activity != null) {
      oTitle.subscribe(title -> Snackbar.make(activity.findViewById(android.R.id.content),
          title, Snackbar.LENGTH_LONG).show());
    }
  }

  /**
   * {inherit docs}
   */
  @Override public void showToast(@StringRes int idString) {
    Toast.makeText(baseApp, baseApp.getString(idString),
        Toast.LENGTH_LONG).show();
  }

  /**
   * {inherit docs}
   */
  @Override public void showSnackBar(@StringRes int idString) {
    Activity activity = baseApp.getLiveActivity();
    if (activity != null) {
      Snackbar.make(activity.findViewById(android.R.id.content),
          baseApp.getString(idString), Snackbar.LENGTH_LONG).show();
    }
  }

  /**
   * {inherit docs}
   */
  @Override public void showFcmNotification(Observable<Message> oMessage) {
    Observable<String> oNotification = oMessage
        .map(message ->
          message.payload().getString("title")
              + System.getProperty("line.separator")
              + message.payload().getString("body")
        );

    showToast(oNotification);
  }
}
