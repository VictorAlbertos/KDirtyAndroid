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

import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.presenter.SyncView;
import app.presentation.foundation.transformations.Transformations;
import app.presentation.sections.TransformationsMock;
import app.presentation.sections.users.list.UsersFragment;
import app.presentation.sections.users.search.SearchUserFragment;
import org.base_app_android.R;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class DashboardPresenterTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  Transformations transformations = Mockito.spy(TransformationsMock.class);
  @Mock DashboardPresenter.View view;
  @Mock Notifications notifications;
  @Mock SyncView syncView;

  private DashboardPresenter dashboardPresenterUT;

  @Before public void init() {
    dashboardPresenterUT = new DashboardPresenter(transformations,
        notifications, syncView, null);
  }

  @Test public void Verify_OnBindView() {
    dashboardPresenterUT.onBindView(view);

    verify(view).setNavigationItemSelectedListener(any());
    verify(view).replaceFragment(any());
  }

  @Test public void Verify_OnBindView_With_Current_Fragment() {
    when(view.currentFragment()).thenReturn(new UsersFragment());
    dashboardPresenterUT.onBindView(view);

    verify(view).setNavigationItemSelectedListener(any());
    verify(view, never()).replaceFragment(any());
  }

  @Test
  public void When_Call_ReplaceFragmentIfItIsNotCurrentOne_And_Current_Fragment_Is_The_Current_One_Then_Do_Not_Replace_It() {
    when(view.currentFragment()).thenReturn(new UsersFragment());
    dashboardPresenterUT.onBindView(view);
    dashboardPresenterUT.replaceFragmentIfItIsNotCurrentOne(R.id.drawer_users);

    verify(view, never()).setCheckedItemMenu(any(int.class));
    verify(view, never()).setTitleActionBar(any(int.class));
    verify(view, never()).replaceFragment(any());
  }

  @Test
  public void When_Call_ReplaceFragmentIfItIsNotCurrentOne_And_Current_Fragment_Is_Not_The_Current_One_Then_Replace_It() {
    when(view.currentFragment()).thenReturn(new SearchUserFragment());
    dashboardPresenterUT.onBindView(view);
    dashboardPresenterUT.replaceFragmentIfItIsNotCurrentOne(R.id.drawer_users);

    verify(view).setCheckedItemMenu(R.id.drawer_users);
    verify(view).setTitleActionBar(R.string.users);
    verify(view).replaceFragment(UsersFragment.class);
  }
}
