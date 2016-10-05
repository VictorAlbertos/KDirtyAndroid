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

import android.view.View;
import android.widget.EditText;
import app.data.sections.users.User;
import app.presentation.foundation.views.BaseFragment;
import app.presentation.foundation.views.LayoutResFragment;
import app.presentation.sections.users.UserViewGroup;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import org.base_app_android.R;
import rx.Observable;

@LayoutResFragment(R.layout.user_search_fragment)
public final class SearchUserFragment extends BaseFragment<SearchUserPresenter>
    implements SearchUserPresenter.View {
  @BindView(R.id.user_view_group) UserViewGroup userViewGroup;
  @BindView(R.id.et_name) EditText etName;
  @BindView(R.id.bt_find_user) View btFindUser;

  @Override protected void initViews() {
    // Do nothing.  Exists to satisfy BaseFragment.
  }

  @Override protected void injectDagger() {
    getApplicationComponent().inject(this);
  }

  @Override public Observable<Void> clicksSearchUser() {
    return RxView.clicks(btFindUser);
  }

  @Override public String username() {
    return etName.getText().toString();
  }

  @Override public void showUser(User user) {
    userViewGroup.setVisibility(View.VISIBLE);
    userViewGroup.bind(user, 0, 0);
  }

}
