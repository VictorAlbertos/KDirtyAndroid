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

package app.presentation.sections.users.detail;

import app.data.sections.users.User;
import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.presenter.Presenter;
import app.presentation.foundation.presenter.SyncView;
import app.presentation.foundation.presenter.ViewPresenter;
import app.presentation.foundation.transformations.Transformations;
import app.presentation.sections.users.UsersWireframe;
import javax.inject.Inject;

final class UserPresenter extends Presenter<UserPresenter.View> {
  private final UsersWireframe wireframe;

  @Inject UserPresenter(Transformations transformations, UsersWireframe wireframe,
      Notifications notifications, SyncView syncView) {
    super(transformations, notifications, syncView);
    this.wireframe = wireframe;
  }

  @Override public void onBindView(View view) {
    super.onBindView(view);

    wireframe.getUserScreen()
        .compose(transformations.safely())
        .compose(transformations.loading())
        .compose(transformations.reportOnSnackBar())
        .subscribe(view::showUser);
  }


  interface View extends ViewPresenter {
    void showUser(User user);
  }
}
