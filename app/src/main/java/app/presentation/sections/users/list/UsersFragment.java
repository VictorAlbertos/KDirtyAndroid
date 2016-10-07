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

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import app.data.sections.users.User;
import app.presentation.foundation.views.BaseFragment;
import app.presentation.foundation.views.LayoutResFragment;
import app.presentation.sections.users.UserViewGroup;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.util.List;
import miguelbcr.ok_adapters.recycler_view.OkRecyclerViewAdapter;
import miguelbcr.ok_adapters.recycler_view.Pager;
import org.base_app_android.R;

@LayoutResFragment(R.layout.users_fragment)
public final class UsersFragment extends BaseFragment<UsersPresenter> implements
    UsersPresenter.View {
  @BindView(R.id.rv_users) RecyclerView rvUsers;
  @BindView(R.id.srl_users) SwipeRefreshLayout swipeRefreshUsers;
  private OkRecyclerViewAdapter<User, UserViewGroup> adapter;
  private int positionRecyclerState;

  @Override protected void injectDagger() {
    getApplicationComponent().inject(this);
  }

  @Override protected void initViews() {
    adapter = new OkRecyclerViewAdapter<User, UserViewGroup>() {
      @Override protected UserViewGroup onCreateItemView(ViewGroup parent, int viewType) {
        return new UserViewGroup(getActivity());
      }
    };

    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
    adapter.configureGridLayoutManagerForPagination(layoutManager);

    rvUsers.setLayoutManager(layoutManager);
    rvUsers.setAdapter(adapter);

    layoutManager.scrollToPosition(positionRecyclerState);
  }

  @Override public void setUpLoaderPager(List<User> initialLoad,
      Pager.LoaderPager<User> loaderPager) {
    adapter.setPager(R.layout.srv_progress, initialLoad, loaderPager);
  }

  @Override public void setUpRefreshList(Pager.Call<User> call) {
    swipeRefreshUsers.setOnRefreshListener(() -> adapter.resetPager(call));
  }

  @Override public Observable<User> userSelectedClicks() {
    PublishSubject<User> clicks = PublishSubject.create();
    adapter.setOnItemClickListener((user, userViewGroup, position) -> {
      clicks.onNext(user);
    });
    return clicks;
  }

  @Override public void showNewUser(User user) {
    adapter.getAll().add(0, user);
    adapter.notifyDataSetChanged();
  }

  @Override public void hideLoadingOnRefreshList() {
    swipeRefreshUsers.setRefreshing(false);
  }

  @Override public void onDestroyView() {
    positionRecyclerState = ((GridLayoutManager) rvUsers
        .getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    super.onDestroyView();
  }


}
