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

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import app.presentation.foundation.views.BaseActivity;
import app.presentation.foundation.views.FragmentsManager;
import app.presentation.foundation.views.LayoutResActivity;
import butterknife.BindView;
import com.jakewharton.rxbinding2.support.design.widget.RxNavigationView;
import io.reactivex.Observable;
import org.base_app_android.R;

@LayoutResActivity(R.layout.dashboard_activity) public final class DashBoardActivity
    extends BaseActivity<DashboardPresenter> implements DashboardPresenter.View {
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
  @BindView(R.id.navigation_view) NavigationView navigationView;
  private ActionBarDrawerToggle drawerToggle;

  @Override protected void injectDagger() {
    getApplicationComponent().inject(this);
  }

  @Override protected void initViews() {
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    drawerToggle =
        new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
    drawerLayout.addDrawerListener(drawerToggle);
  }

  @Override protected void onDestroy() {
    drawerLayout.removeDrawerListener(drawerToggle);
    super.onDestroy();
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    drawerToggle.syncState();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (drawerToggle.onOptionsItemSelected(item)) {
      return true;
    } else if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public Observable<MenuItem> clicksItemSelected() {
    return RxNavigationView.itemSelections(navigationView);
  }

  @Override public void setCheckedItemMenu(@IdRes int id) {
    navigationView.setCheckedItem(id);
  }

  @Override public void setTitleActionBar(@StringRes int id) {
    if (getSupportActionBar() != null) getSupportActionBar().setTitle(getString(id));
  }

  @Override public boolean replaceFragment(FragmentsManager fragmentsManager,
      Class<? extends Fragment> classFragment) {
    return fragmentsManager.replaceFragment(getSupportFragmentManager(), R.id.fl_fragment,
        classFragment, false);
  }

  @Override public void closeDrawer() {
    drawerLayout.closeDrawers();
  }
}
