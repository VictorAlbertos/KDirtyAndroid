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

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import app.data.sections.users.User;
import app.presentation.foundation.views.BaseActivity;
import app.presentation.foundation.views.LayoutResActivity;
import app.presentation.sections.users.UserViewGroup;
import butterknife.BindView;
import org.base_app_android.R;

@LayoutResActivity(R.layout.user_activity)
public final class UserActivity extends BaseActivity<UserPresenter.View, UserPresenter>
    implements UserPresenter.View {
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.user_view_group) UserViewGroup userViewGroup;

  @Override protected void injectDagger() {
    getApplicationComponent().inject(this);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) onBackPressed();
    return super.onOptionsItemSelected(item);
  }

  @Override protected void initViews() {
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(getString(R.string.user));
  }

  @Override public void showUser(User user) {
    userViewGroup.bind(user, 0, 0);
  }

}
