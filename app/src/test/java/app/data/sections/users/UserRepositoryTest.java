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

package app.data.sections.users;

import app.data.foundation.net.NetworkResponse;
import io.reactivecache2.ReactiveCache;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.observers.TestObserver;
import io.rx_cache2.Reply;
import io.rx_cache2.Source;
import io.victoralbertos.jolyglot.GsonSpeaker;
import io.victoralbertos.mockery.api.Mockery;
import io.victoralbertos.mockery.api.built_in_interceptor.Rx2Retrofit;
import java.io.IOException;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;

public final class UserRepositoryTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule public TemporaryFolder testFolder = new TemporaryFolder();
  private UserRepository userRepositoryUT;

  @Test public void Verify_GetUsers_With_LastIdQueried_Null() {
    mockApiForSuccess();

    TestObserver<List<User>> observer = userRepositoryUT.getUsers(null, false).test();
    observer.awaitTerminalEvent();
    observer.assertNoErrors();
    observer.assertValueCount(1);

    List<User> users = observer.values().get(0);
    assertEquals(users.get(0).getId(), 1);
  }

  @Test public void Verify_GetUsers_Success() {
    mockApiForSuccess();

    int id = 50;

    TestObserver<List<User>> observer = userRepositoryUT.getUsers(id, false).test();
    observer.awaitTerminalEvent();
    observer.assertNoErrors();
    observer.assertValueCount(1);

    List<User> users = observer.values().get(0);
    assertEquals(users.get(0).getId(), id + 1);
  }

  @Test public void Verify_GetUsers_Refresh() {
    mockApiForSuccess();

    TestObserver<Reply<List<User>>> observer1 = userRepositoryUT.getUsersReply(null, false).test();
    observer1.awaitTerminalEvent();

    assertEquals(Source.CLOUD,
        observer1.values().get(0).getSource());

    TestObserver<Reply<List<User>>> observer2 = userRepositoryUT.getUsersReply(null, false).test();
    observer2.awaitTerminalEvent();

    assertEquals(Source.MEMORY,
        observer2.values().get(0).getSource());

    TestObserver<Reply<List<User>>> observer3 = userRepositoryUT.getUsersReply(null, true).test();
    observer3.awaitTerminalEvent();

    assertEquals(Source.CLOUD,
        observer3.values().get(0).getSource());
  }

  @Test public void Verify_GetUsers_Failure() {
    mockApiForFailure();

    TestObserver<List<User>> observer = userRepositoryUT.getUsers(null, false).test();
    observer.awaitTerminalEvent();
    observer.assertError(CompositeException.class);
    observer.assertNoValues();
  }

  @Test public void Verify_SearchByUserName_Success() {
    mockApiForSuccess();

    String username = "username";

    TestObserver<User> observer = userRepositoryUT.searchByUserName(username).test();
    observer.awaitTerminalEvent();
    observer.assertNoErrors();
    observer.assertValueCount(1);

    User user = observer.values().get(0);
    assertEquals(username, user.getLogin());
  }

  @Test public void Verify_SearchByUserName_Failure() {
    mockApiForFailure();

    TestObserver<User> observer = userRepositoryUT.searchByUserName("don't care").test();
    observer.awaitTerminalEvent();
    observer.assertError(IOException.class);
    observer.assertNoValues();

    Throwable error = observer.errors().get(0);
    assertEquals("Mock failure!", error.getMessage());
  }

  @Rx2Retrofit(delay = 0, failurePercent = 0, variancePercentage = 0) interface ApiSuccess
      extends GithubUsersApi {
  }

  @Rx2Retrofit(delay = 0, failurePercent = 100, variancePercentage = 0) interface ApiFailure
      extends GithubUsersApi {
  }

  private void mockApiForSuccess() {
    NetworkResponse networkResponse = new NetworkResponse();

    GithubUsersApi githubUsersApi = new Mockery.Builder<ApiSuccess>()
        .mock(ApiSuccess.class)
        .build();

    ReactiveCache reactiveCache = new ReactiveCache.Builder()
        .using(testFolder.getRoot(), new GsonSpeaker());

    userRepositoryUT = new UserRepository(githubUsersApi,
        networkResponse, reactiveCache);
  }

  private void mockApiForFailure() {
    NetworkResponse networkResponse = new NetworkResponse();

    GithubUsersApi githubUsersApi = new Mockery.Builder<ApiFailure>()
        .mock(ApiFailure.class)
        .build();

    ReactiveCache reactiveCache = new ReactiveCache.Builder()
        .using(testFolder.getRoot(), new GsonSpeaker());

    userRepositoryUT = new UserRepository(githubUsersApi,
        networkResponse, reactiveCache);
  }
}