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

import android.support.annotation.StringRes;
import io.reactivex.Observable;
import rx_fcm.Message;

/**
 * A centralized notification pipeline approach. This is indirection layer allows us to run unit tests without mocking the Android platform.
 */
public interface Notifications {
  /**
   * For output toast messages from the data layer.
   */
  void showToast(Observable<String> oTitle);

  /**
   * For output output snackbar messages from the data layer.
   */
  void showSnackBar(Observable<String> oTitle);

  /**
   * For output output toast messages from the presentation layer.
   */
  void showToast(@StringRes int idString);

  /**
   * For output snackbar messages from the presentation layer.
   */
  void showSnackBar(@StringRes int idString);

  /**
   * For output toast messages received in fcm push notifications from the presentation layer.
   */
  void showFcmNotification(Observable<Message> oMessage);
}
