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

package app.presentation.sections.dashboard;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import app.data.sections.users.UserRepository;
import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.presenter.Presenter;
import app.presentation.foundation.presenter.SyncView;
import app.presentation.foundation.presenter.ViewPresenter;
import app.presentation.foundation.transformations.Transformations;
import app.presentation.foundation.views.FragmentsManager;
import app.presentation.sections.users.list.UsersFragment;
import app.presentation.sections.users.search.SearchUserFragment;
import io.reactivex.Observable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.base_app_android.R;

final class DashboardPresenter extends Presenter<DashboardPresenter.View> {
  private final UserRepository userRepository;
  private final FragmentsManager fragmentsManager;

  private static final Map<Integer, ItemMenu> ITEMS_MENU;

  static {
    ITEMS_MENU = new HashMap<>();
    ITEMS_MENU.put(R.id.drawer_users, ItemMenu.create(UsersFragment.class, R.string.users));
    ITEMS_MENU.put(R.id.drawer_find_user,
        ItemMenu.create(SearchUserFragment.class, R.string.find_user));
  }

  @Inject DashboardPresenter(Transformations transformations, Notifications notifications,
      SyncView syncView, UserRepository userRepository, FragmentsManager fragmentsManager) {
    super(transformations, notifications, syncView);
    this.userRepository = userRepository;
    this.fragmentsManager = fragmentsManager;
  }

  @Override public void onBindView(View view) {
    super.onBindView(view);

    replaceDrawerFragment(R.id.drawer_users);

    view.clicksItemSelected()
        .subscribe(menuItem -> {
          if (menuItem.getItemId() == R.id.drawer_mock_user) {
            userRepository.mockAFcmNotification().subscribe();
            return;
          }

          replaceDrawerFragment(menuItem.getItemId());
          view.closeDrawer();
        });
  }

  @VisibleForTesting void replaceDrawerFragment(@IdRes int idSelectedMenu) {
    ItemMenu itemMenu = ITEMS_MENU.get(idSelectedMenu);
    Class<? extends Fragment> classFragment = itemMenu.classFragment();

    if (view.replaceFragment(fragmentsManager, classFragment)) {
      view.setCheckedItemMenu(idSelectedMenu);
      view.setTitleActionBar(itemMenu.resTitle());
    }
  }

  interface View extends ViewPresenter {

    boolean replaceFragment(FragmentsManager fragmentsManager,
        Class<? extends Fragment> classFragment);

    Observable<MenuItem> clicksItemSelected();

    void setCheckedItemMenu(@IdRes int id);

    void setTitleActionBar(@StringRes int id);

    void closeDrawer();
  }
}
