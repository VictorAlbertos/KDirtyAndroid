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

package app.data.foundation.fcm;

import app.presentation.foundation.BaseApp;
import io.reactivex.Observable;
import rx_fcm.FcmRefreshTokenReceiver;
import rx_fcm.TokenUpdate;

/**
 * Receive periodic updates for a particular Fcm token.
 */
public final class FcmTokenReceiver implements FcmRefreshTokenReceiver {

  @Override public void onTokenReceive(Observable<TokenUpdate> oTokenUpdate) {
    oTokenUpdate.subscribe(tokenUpdate -> {
      BaseApp baseApp = (BaseApp) tokenUpdate.getApplication();
      baseApp.getPresentationComponent().inject(this);

      // TODO Make use of repository to update the token on the server.
    });
  }

}
