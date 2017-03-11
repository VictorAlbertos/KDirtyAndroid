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

package app.presentation.sections.users;

import android.content.Intent;
import app.data.foundation.Ignore;
import app.data.foundation.WireframeRepository;
import app.data.sections.users.User;
import app.presentation.foundation.BaseApp;
import app.presentation.sections.users.detail.UserActivity;
import io.reactivex.Single;
import javax.inject.Inject;

public class UsersWireframe {
  private final WireframeRepository wireframeRepository;
  private final BaseApp baseApp;

  @Inject public UsersWireframe(BaseApp baseApp,
      WireframeRepository wireframeRepository) {
    this.baseApp = baseApp;
    this.wireframeRepository = wireframeRepository;
  }

  public Single<Ignore> userScreen(User user) {
    return wireframeRepository
        .put(UserActivity.class.getName(), user)
        .map(_I -> {
          baseApp.getLiveActivity()
              .startActivity(new Intent(baseApp, UserActivity.class));
          return _I;
        });
  }

  public Single<User> getUserScreen() {
    return wireframeRepository
        .<User>get(UserActivity.class.getName());
  }
}
