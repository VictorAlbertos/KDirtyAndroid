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

package app.data.foundation.fcm;

import android.os.Bundle;
import app.data.foundation.MockModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rx_fcm.Message;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public final class FcmMessageReceiverTest {
  @Mock Bundle bundle;
  private Message message;
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  private FcmMessageReceiver fcmMessageReceiverUT;

  @Before public void init() {
    fcmMessageReceiverUT = new FcmMessageReceiver();

    when(bundle.getString("title")).thenReturn("title");
    when(bundle.getString("body")).thenReturn("body");
    when(bundle.getString("payload")).thenReturn("{\"s1\":\"s1\"}");
    message = new Message(null, bundle, null, null);
  }

  @Test public void Verify_Model() {
    MockModel mockModel = fcmMessageReceiverUT
        .getModel(MockModel.class, message);
    assertEquals(mockModel.getS1(), "s1");
  }

}
