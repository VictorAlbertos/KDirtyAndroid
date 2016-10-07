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

package app.presentation.sections.users.search;

import android.support.annotation.VisibleForTesting;
import app.data.foundation.Ignore;
import app.data.sections.users.User;
import app.data.sections.users.UserRepository;
import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.presenter.Presenter;
import app.presentation.foundation.presenter.SyncView;
import app.presentation.foundation.presenter.ViewPresenter;
import app.presentation.foundation.transformations.Transformations;
import io.reactivex.Observable;
import javax.inject.Inject;
import org.base_app_android.R;

final class SearchUserPresenter extends Presenter<SearchUserPresenter.View> {
  private final UserRepository userRepository;
  private User userState;

  @Inject public SearchUserPresenter(Transformations transformations,
      UserRepository userRepository, SyncView syncView,
      Notifications notifications) {
    super(transformations, notifications, syncView);
    this.userRepository = userRepository;
  }

  @Override public void onBindView(View view) {
    super.onBindView(view);
    if (userState != null) {
      view.showUser(userState);
    }

    view.clicksSearchUser()
        .subscribe(ignored -> getUserByUserName(view.username()));
  }

  @VisibleForTesting void getUserByUserName(String username) {
    if (username.isEmpty()) {
      notifications.showSnackBar(R.string.fill_missing_fields);
      return;
    }

    userRepository.searchByUserName(username)
        .compose(transformations.safely())
        .compose(transformations.loading())
        .compose(transformations.reportOnSnackBar())
        .subscribe(user -> {
          userState = user;
          view.showUser(user);
        });
  }

  interface View extends ViewPresenter {
    void showUser(User user);

    Observable<Ignore> clicksSearchUser();

    String username();
  }
}
