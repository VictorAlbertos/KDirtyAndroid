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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ErrorAdapterTest {
  private ErrorAdapter errorAdapterUT;

  @Before public void init() {
    errorAdapterUT = new ErrorAdapter();
  }

  @Test public void Verify_Adapt() {
    String json = "{\"message\":\"such a nice error\"}";
    String prettified = errorAdapterUT.adapt(json);
    assertEquals(prettified, "such a nice error");
  }
}