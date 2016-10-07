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
import app.presentation.foundation.views.FragmentsManager;
import app.presentation.sections.TransformationsMock;
import app.presentation.sections.users.search.SearchUserFragment;
import io.reactivex.Observable;
import org.base_app_android.R;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class DashboardPresenterTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  Transformations transformations = Mockito.spy(TransformationsMock.class);
  @Mock DashboardPresenter.View view;
  @Mock Notifications notifications;
  @Mock SyncView syncView;
  @Mock FragmentsManager fragmentsManager;

  private DashboardPresenter dashboardPresenterUT;

  @Before public void init() {
    dashboardPresenterUT = new DashboardPresenter(transformations,
        notifications, syncView, null, fragmentsManager);
    when(view.clicksItemSelected()).thenReturn(Observable.never());
  }

  @Test public void Verify_OnBindView() {
    dashboardPresenterUT.onBindView(view);

    verify(view).clicksItemSelected();
    verify(view).replaceFragment(any(FragmentsManager.class), any());
  }

  @Test public void Verify_OnBindView_With_Current_Fragment() {
    dashboardPresenterUT.onBindView(view);

    verify(view).clicksItemSelected();
    verify(view).replaceFragment(any(FragmentsManager.class), any());
  }

  @Test public void When_Call_replaceDrawerFragment_And_Current_Fragment_Is_The_Current_One_Then_Do_Not_Replace_It() {
    dashboardPresenterUT.onBindView(view);
    when(view.replaceFragment(any(FragmentsManager.class), any())).thenReturn(false);
    dashboardPresenterUT.replaceDrawerFragment(R.id.drawer_users);

    verify(view, never()).setCheckedItemMenu(any(int.class));
    verify(view, never()).setTitleActionBar(any(int.class));
    verify(view, atLeastOnce()).replaceFragment(any(fragmentsManager.getClass()), any());
  }

  @Test public void When_Call_replaceDrawerFragment_And_Current_Fragment_Is_Not_The_Current_One_Then_Replace_It() {
    dashboardPresenterUT.onBindView(view);
    when(view.replaceFragment(any(FragmentsManager.class), any())).thenReturn(true);
    dashboardPresenterUT.replaceDrawerFragment(R.id.drawer_find_user);

    verify(view).setCheckedItemMenu(R.id.drawer_find_user);
    verify(view).setTitleActionBar(R.string.find_user);
    verify(view).replaceFragment(fragmentsManager, SearchUserFragment.class);
  }
}
