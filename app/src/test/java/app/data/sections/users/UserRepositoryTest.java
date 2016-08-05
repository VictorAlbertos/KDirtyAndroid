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
import io.reactivecache.ReactiveCache;
import io.rx_cache.Reply;
import io.rx_cache.RxCacheException;
import io.rx_cache.Source;
import io.victoralbertos.jolyglot.GsonSpeaker;
import io.victoralbertos.mockery.api.Mockery;
import io.victoralbertos.mockery.api.built_in_interceptor.RxRetrofit;
import java.io.IOException;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;

public final class UserRepositoryTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule public TemporaryFolder testFolder = new TemporaryFolder();
  private UserRepository userRepositoryUT;

  @Test public void Verify_GetUsers_With_LastIdQueried_Null() {
    mockApiForSuccess();

    TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
    userRepositoryUT.getUsers(null, false).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);

    List<User> users = subscriber.getOnNextEvents().get(0);
    assertEquals(users.get(0).id(), 1);
  }

  @Test public void Verify_GetUsers_Success() {
    mockApiForSuccess();

    int id = 50;

    TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
    userRepositoryUT.getUsers(id, false).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);

    List<User> users = subscriber.getOnNextEvents().get(0);
    assertEquals(users.get(0).id(), id+1);
  }

  @Test public void Verify_GetUsers_Refresh() {
    mockApiForSuccess();

    TestSubscriber<Reply<List<User>>> subscriber1 = new TestSubscriber<>();
    userRepositoryUT.getUsersReply(null, false).subscribe(subscriber1);
    subscriber1.awaitTerminalEvent();

    assertEquals(Source.CLOUD,
        subscriber1.getOnNextEvents().get(0).getSource());

    TestSubscriber<Reply<List<User>>> subscriber2 = new TestSubscriber<>();
    userRepositoryUT.getUsersReply(null, false).subscribe(subscriber2);
    subscriber2.awaitTerminalEvent();

    assertEquals(Source.MEMORY,
        subscriber2.getOnNextEvents().get(0).getSource());

    TestSubscriber<Reply<List<User>>> subscriber3 = new TestSubscriber<>();
    userRepositoryUT.getUsersReply(null, true).subscribe(subscriber3);
    subscriber3.awaitTerminalEvent();

    assertEquals(Source.CLOUD,
        subscriber3.getOnNextEvents().get(0).getSource());
  }

  @Test public void Verify_GetUsers_Failure() {
    mockApiForFailure();

    TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
    userRepositoryUT.getUsers(null, false).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(RxCacheException.class);
    subscriber.assertNoValues();

    Throwable error = subscriber.getOnErrorEvents().get(0);
    assertEquals(error.getCause().getMessage(),
        "Mock failure!");
  }

  @Test public void Verify_SearchByUserName_Success() {
    mockApiForSuccess();

    String username = "username";

    TestSubscriber<User> subscriber = new TestSubscriber<>();
    userRepositoryUT.searchByUserName(username).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);

    User user = subscriber.getOnNextEvents().get(0);
    assertEquals(username, user.login());
  }

  @Test public void Verify_SearchByUserName_Failure() {
    mockApiForFailure();

    TestSubscriber<User> subscriber = new TestSubscriber<>();
    userRepositoryUT.searchByUserName("don't care").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(IOException.class);
    subscriber.assertNoValues();

    Throwable error = subscriber.getOnErrorEvents().get(0);
    assertEquals("Mock failure!", error.getMessage());
  }

  @RxRetrofit(delay = 0, failurePercent = 0, variancePercentage = 0)
  interface ApiSuccess extends GithubUsersApi {}

  @RxRetrofit(delay = 0, failurePercent = 100, variancePercentage = 0)
  interface ApiFailure extends GithubUsersApi {}

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