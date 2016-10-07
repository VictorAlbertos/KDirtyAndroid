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

package app.presentation.sections.users.list;

import android.support.annotation.VisibleForTesting;
import app.data.foundation.fcm.FcmMessageReceiver;
import app.data.sections.users.User;
import app.data.sections.users.UserRepository;
import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.presenter.Presenter;
import app.presentation.foundation.presenter.SyncView;
import app.presentation.foundation.presenter.ViewPresenter;
import app.presentation.foundation.transformations.Transformations;
import app.presentation.sections.users.UsersWireframe;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import miguelbcr.ok_adapters.recycler_view.Pager;
import rx_fcm.Message;

final class UsersPresenter extends Presenter<UsersPresenter.View> {
  private final UserRepository repository;
  @VisibleForTesting final List<User> usersState;
  private final UsersWireframe wireframe;

  @Inject UsersPresenter(Transformations transformations,
      UserRepository repository, UsersWireframe wireframe,
      SyncView syncView, Notifications notifications) {
    super(transformations, notifications, syncView);
    this.repository = repository;
    this.usersState = new ArrayList<>();
    this.wireframe = wireframe;
  }

  @Override public void onBindView(View view) {
    super.onBindView(view);

    view.setUpLoaderPager(usersState,
        lastItem -> callback -> nextPage(lastItem, callback));

    view.setUpRefreshList(this::refreshList);

    view.userSelectedClicks()
       .flatMap(wireframe::userScreen)
        .subscribe();
  }

  @VisibleForTesting void nextPage(User user, Pager.Callback<User> callback) {
    Integer id = null;
    if (user != null) id = user.id();

    repository.getUsers(id, false)
        .compose(transformations.safely())
        .compose(transformations.reportOnSnackBar())
        .subscribe(users -> {
          callback.supply(users);
          usersState.addAll(users);
        });
  }

  @VisibleForTesting void refreshList(Pager.Callback<User> callback) {
    repository.getUsers(null, true)
        .compose(transformations.safely())
        .compose(transformations.reportOnSnackBar())
        .subscribe(users -> {
          callback.supply(users);
          usersState.clear();
          usersState.addAll(users);
          view.hideLoadingOnRefreshList();
        });
  }

  @Override public void onTargetNotification(Observable<Message> ignore) {
    repository.getRecentUser()
        .compose(transformations.safely())
        .compose(transformations.reportOnSnackBar())
        .subscribe(user -> {
          usersState.add(0, user);
          view.showNewUser(user);
        });
  }

  @Override public boolean matchesTarget(String key) {
    return FcmMessageReceiver.USERS_FCM.equals(key);
  }

  interface View extends ViewPresenter {
    void setUpLoaderPager(List<User> initialLoad, Pager.LoaderPager<User> loaderPager);

    void setUpRefreshList(Pager.Call<User> call);

    Observable<User> userSelectedClicks();

    void showNewUser(User user);

    void hideLoadingOnRefreshList();
  }
}
