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

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import app.data.foundation.Ignore;
import app.data.foundation.fcm.FcmMessageReceiver;
import app.data.foundation.net.NetworkResponse;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import io.reactivecache2.ProviderGroup;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;
import io.rx_cache2.Reply;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx_fcm.internal.RxFcmMock;

public class UserRepository {
  private static final int FIRST_DEFAULT_ID = 0, USERS_PER_PAGE = 50;
  private final GithubUsersApi githubUsersApi;
  private final ProviderGroup<List<User>> cacheProvider;
  private final NetworkResponse networkResponse;

  @Inject public UserRepository(GithubUsersApi githubUsersApi, NetworkResponse networkResponse,
      ReactiveCache reactiveCache) {
    this.githubUsersApi = githubUsersApi;
    this.networkResponse = networkResponse;
    this.cacheProvider = reactiveCache.<List<User>>providerGroup()
        .withKey("users");
  }

  @RxLogObservable
  public Observable<List<User>> getUsers(Integer lastIdQueried, final boolean refresh) {
    return getUsersReply(lastIdQueried, refresh)
        .map(Reply::getData);
  }


  @RxLogObservable
  public Observable<User> getRecentUser() {
    return getUsersReply(FIRST_DEFAULT_ID, false)
        .map(Reply::getData)
        .map(users -> users.get(0));
  }

  @VisibleForTesting
  Observable<Reply<List<User>>> getUsersReply(Integer lastIdQueried, final boolean refresh) {
    int safeId = lastIdQueried == null ? FIRST_DEFAULT_ID : lastIdQueried;

    Observable<List<User>> apiCall = githubUsersApi
        .getUsers(safeId, USERS_PER_PAGE)
        .compose(networkResponse.process());

    return refresh ? apiCall.compose(cacheProvider.replaceAsReply(safeId))
        : apiCall.compose(cacheProvider.readWithLoaderAsReply(safeId));
  }

  @RxLogObservable
  public Observable<User> searchByUserName(final String username) {
    return githubUsersApi
        .getUserByName(username)
        .compose(networkResponse.process());
  }

  @RxLogObservable
  public Observable<Ignore> addNewUser(User user) {
    return cacheProvider.read(FIRST_DEFAULT_ID)
        .doOnNext(users -> users.add(0, user))
        .compose(cacheProvider.replace(FIRST_DEFAULT_ID))
        .map(ignore -> Ignore.Get);
  }

  @RxLogObservable
  public Observable<Ignore> mockAFcmNotification() {
    return Observable.just(Ignore.Get)
        .delay(3, TimeUnit.SECONDS)
        .flatMap(ignore -> {
          Bundle payload = new Bundle();

          payload.putString("payload", "{\n"
              + "\t\"id\":\"0\",\n"
              + "\t\"login\": \"FcmUserMock\",\n"
              + "\t\"avatar_url\":\"https://cdn0.iconfinder.com/data/icons/octicons/1024/mark-github-256.png\"\n"
              + "}");
          payload.putString("rx_fcm_key_target", FcmMessageReceiver.USERS_FCM);
          payload.putString("title", "Received a new user mock");
          payload.putString("body", "Go to users screen and check it!");

          RxFcmMock.Notifications.newNotification(payload);

          return Observable.just(Ignore.Get);
        });
  }

}
