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

import app.data.sections.users.User;
import app.data.sections.users.UserRepository;
import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.presenter.SyncView;
import app.presentation.foundation.transformations.Transformations;
import app.presentation.sections.TransformationsMock;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class SearchUserPresenterTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  Transformations transformations = Mockito.spy(TransformationsMock.class);
  @Mock UserRepository userRepository;
  @Mock Notifications notifications;
  @Mock SyncView syncView;
  @Mock SearchUserPresenter.View view;
  private SearchUserPresenter searchUserPresenterUT;

  @Before public void init() {
    searchUserPresenterUT = new SearchUserPresenter(transformations,
        userRepository, syncView, notifications);
    when(view.clicksSearchUser()).thenReturn(Observable.never());
  }

  @Test public void Verify_OnBindView() {
    searchUserPresenterUT.onBindView(view);

    verify(view).clicksSearchUser();
    verify(view, never()).showUser(any());
  }

  @Test public void When_Call_OnBindView_With_User_State_Then_ShowUser_Is_Called() {
    mockSuccessResponse();
    searchUserPresenterUT.onBindView(view);
    searchUserPresenterUT.getUserByUserName("noEmpty");
    verify(view).showUser(any());

    searchUserPresenterUT.onBindView(view);
    verify(view, times(2)).showUser(any());
  }

  @Test public void When_Call_GetUserByName_With_Empty_String_Then_ShowError() {
    searchUserPresenterUT.onBindView(view);
    searchUserPresenterUT.getUserByUserName("");

    verify(userRepository, never()).searchByUserName(any());
    verify(notifications).showSnackBar(any(int.class));
  }

  @Test public void Verify_GetUserByName_With_Success_Response() {
    mockSuccessResponse();
    searchUserPresenterUT.onBindView(view);
    searchUserPresenterUT.getUserByUserName("noEmpty");

    verify(notifications, never()).showSnackBar(any());
    verify(transformations).safely();
    verify(transformations).loading();
    verify(transformations).reportOnSnackBar();
    verify(view).showUser(any());
  }

  @Test public void Verify_GetUserByName_With_Error_Response() {
    mockErrorResponse();
    searchUserPresenterUT.onBindView(view);
    searchUserPresenterUT.getUserByUserName("noEmpty");

    verify(transformations).safely();
    verify(transformations).loading();
    verify(transformations).reportOnSnackBar();

    verify(view, never()).showUser(any());
  }

  private void mockSuccessResponse() {
    when(userRepository.searchByUserName(any()))
        .thenReturn(Observable.just(User.create(1, "user", "avatar")));
  }

  private void mockErrorResponse() {
    when(userRepository.searchByUserName(any()))
        .thenReturn(Observable.error(new RuntimeException()));
  }

}
