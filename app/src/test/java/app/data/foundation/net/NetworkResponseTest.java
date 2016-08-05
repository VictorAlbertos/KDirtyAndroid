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

import app.data.foundation.MockModel;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Response;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

public final class NetworkResponseTest {
  private NetworkResponse networkResponseUT;

  @Before public void init() {
    networkResponseUT = new NetworkResponse();
  }

  @Test public void Verify_On_Success() {
    MockModel model = new MockModel();
    Observable<MockModel> oModel = Observable
        .just(Response.success(model))
        .compose(networkResponseUT.process());

    TestSubscriber<MockModel> subscriber = new TestSubscriber<>();
    oModel.subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    subscriber.assertValue(model);
  }

  @Test public void Verify_On_Error() {
    String jsonError = "{\"message\":\"such a nice error\"}";
    ResponseBody responseBody = ResponseBody
        .create(MediaType.parse("application/json"), jsonError);

    Observable<MockModel> oModel = Observable
        .just(Response.<MockModel>error(404, responseBody))
        .compose(networkResponseUT.process());

    TestSubscriber<MockModel> subscriber = new TestSubscriber<>();
    oModel.subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoValues();
    Throwable error = subscriber.getOnErrorEvents().get(0);
    assertEquals("such a nice error", error.getMessage());
  }
}