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

package app.data.foundation.net;

import javax.inject.Inject;
import retrofit2.Response;
import rx.Observable;

/**
 * Process a network response. On success returns the associated data. On error throws a {@link
 * NetworkException} to be able to detect it as a custom exception to be handled properly by the
 * downstream.
 */
public final class NetworkResponse {
  private final ErrorAdapter errorAdapter;

  @Inject public NetworkResponse() {
    this.errorAdapter = new ErrorAdapter();
  }

  public <T> Observable.Transformer<Response<T>, T> process() {
    return oResponse -> oResponse
        .flatMap(response -> {
          if (response.isSuccessful()) return Observable.just(response.body());
          try {
            String error = errorAdapter.adapt(response.errorBody().string());
            return Observable.error(new NetworkException(error));
          } catch (java.lang.Exception exception) {
            return Observable.error(new RuntimeException(exception));
          }
        });
  }

  public static class NetworkException extends RuntimeException {

    public NetworkException(String detailMessage) {
      super(detailMessage);
    }
  }
}
