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

import app.data.sections.users.User;
import app.data.sections.users.UserRepository;
import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.presenter.SyncView;
import app.presentation.foundation.transformations.Transformations;
import app.presentation.sections.TransformationsMock;
import app.presentation.sections.users.UsersWireframe;
import io.reactivex.Observable;
import java.util.Arrays;
import miguelbcr.ok_adapters.recycler_view.Pager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class UsersPresenterTest {
  private final static int USER_ID = 1;
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  Transformations transformations = Mockito.spy(TransformationsMock.class);
  @Mock UserRepository userRepository;
  @Mock UsersWireframe wireframe;
  @Mock UsersPresenter.View view;
  @Mock Pager.Callback<User> callback;
  @Mock Notifications notifications;
  @Mock SyncView syncView;
  private UsersPresenter usersPresenterUT;

  @Before public void init() {
    usersPresenterUT = new UsersPresenter(transformations, userRepository,
        wireframe, syncView, notifications);
    when(view.userSelectedClicks()).thenReturn(Observable.never());
  }

  @Test public void When_Call_OnBindView_Then_SetUpLoaderPager_Is_Called() {
    usersPresenterUT.onBindView(view);

    verify(view).setUpLoaderPager(any(), any());
  }

  @Test public void When_Call_OnBindView_Then_SetUpRefreshList_Is_Called() {
    usersPresenterUT.onBindView(view);

    verify(view).setUpRefreshList(any());
  }

  @Test public void When_Call_OnBindView_Then_UserSelectedClicks_Is_Called() {
    usersPresenterUT.onBindView(view);

    verify(view).userSelectedClicks();
  }

  @Test public void When_Call_NextPage_Second_Time_Then_UsersState_Preserve_Users() {
    mockSuccessResponse();
    usersPresenterUT.onBindView(view);
    usersPresenterUT.nextPage(aUser(), callback);
    usersPresenterUT.nextPage(aUser(), callback);

    assertEquals(usersPresenterUT.usersState.size(), 2);
  }

  @Test public void When_Call_RefreshList_Then_UsersState_Is_Cleared() {
    mockSuccessResponse();
    usersPresenterUT.onBindView(view);
    usersPresenterUT.nextPage(aUser(), callback);
    usersPresenterUT.nextPage(aUser(), callback);
    usersPresenterUT.refreshList(callback);

    assertEquals(usersPresenterUT.usersState.size(), 1);
  }

  @Test public void When_Call_NextPage_With_Null_User_Then_Id_Is_Null() {
    mockSuccessResponse();
    usersPresenterUT.onBindView(view);

    usersPresenterUT.nextPage(null, callback);
    verify(userRepository).getUsers(null, false);
  }

  @Test public void Verify_NextPage_With_Success_Response() {
    mockSuccessResponse();
    usersPresenterUT.onBindView(view);
    usersPresenterUT.nextPage(aUser(), callback);

    verify(userRepository).getUsers(USER_ID, false);
    verify(transformations).safely();
    verify(transformations).reportOnSnackBar();
    verify(callback).supply(any());
  }

  @Test public void Verify_NextPage_With_Error_Response() {
    mockErrorResponse();
    usersPresenterUT.onBindView(view);
    usersPresenterUT.nextPage(aUser(), callback);

    verify(userRepository).getUsers(USER_ID, false);
    verify(transformations).safely();
    verify(transformations).reportOnSnackBar();
    verify(callback, never()).supply(any());
  }

  @Test public void Verify_RefreshList_With_Success_Response() {
    mockSuccessResponse();
    usersPresenterUT.onBindView(view);
    usersPresenterUT.refreshList(callback);

    verify(userRepository).getUsers(null, true);
    verify(transformations).safely();
    verify(transformations).reportOnSnackBar();
    verify(callback).supply(any());
    assertEquals(usersPresenterUT.usersState.size(), 1);
  }

  @Test public void Verify_RefreshList_With_Error_Response() {
    mockErrorResponse();
    usersPresenterUT.onBindView(view);
    usersPresenterUT.refreshList(callback);

    verify(userRepository).getUsers(null, true);
    verify(transformations).safely();
    verify(transformations).reportOnSnackBar();
    verify(callback, never()).supply(any());
  }

  @Test public void Verify_On_Target_Notification() {
    usersPresenterUT.onBindView(view);

    User user = aUser();
    when(userRepository.getRecentUser())
        .thenReturn(Observable.just(user));

    usersPresenterUT.onTargetNotification(null);

    verify(userRepository).getRecentUser();
    verify(transformations).safely();
    verify(transformations).reportOnSnackBar();

    assertEquals(1, usersPresenterUT.usersState.size());
    assertEquals(user, usersPresenterUT.usersState.get(0));

    verify(view).showNewUser(any());
  }

  private void mockSuccessResponse() {
    when(userRepository.getUsers(any(), any(boolean.class)))
        .thenReturn(Observable.just(Arrays.asList(aUser())));
  }

  private void mockErrorResponse() {
    when(userRepository.getUsers(any(), any(boolean.class)))
        .thenReturn(Observable.error(new RuntimeException()));
  }

  private User aUser() {
    return User.create(USER_ID, "user", "avatar");
  }
}
