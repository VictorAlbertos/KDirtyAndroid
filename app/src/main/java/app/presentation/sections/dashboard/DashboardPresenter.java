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
import app.presentation.foundation.widgets.Notifications;
import app.presentation.foundation.presenter.Presenter;
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
  private final FragmentsManager fragmentsManager;

  private static final Map<Integer, ItemMenu> ITEMS_MENU;

  static {
    ITEMS_MENU = new HashMap<>();
    ITEMS_MENU.put(R.id.drawer_users, new ItemMenu(UsersFragment.class, R.string.users));
    ITEMS_MENU.put(R.id.drawer_find_user,
        new ItemMenu(SearchUserFragment.class, R.string.find_user));
  }

  @Inject DashboardPresenter(Transformations transformations, Notifications notifications,
      FragmentsManager fragmentsManager) {
    super(transformations, notifications);
    this.fragmentsManager = fragmentsManager;
  }

  @Override public void onBindView(View view) {
    super.onBindView(view);

    replaceDrawerFragment(R.id.drawer_users);

    view.clicksItemSelected()
        .subscribe(menuItem -> {
          replaceDrawerFragment(menuItem.getItemId());
          view.closeDrawer();
        });
  }

  @VisibleForTesting void replaceDrawerFragment(@IdRes int idSelectedMenu) {
    ItemMenu itemMenu = ITEMS_MENU.get(idSelectedMenu);
    Class<? extends Fragment> classFragment = itemMenu.getClassFragment();

    if (view.replaceFragment(fragmentsManager, classFragment)) {
      view.setCheckedItemMenu(idSelectedMenu);
      view.setTitleActionBar(itemMenu.getResTitle());
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
