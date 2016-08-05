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

import com.google.gson.JsonSyntaxException;
import io.victoralbertos.jolyglot.GsonSpeaker;
import io.victoralbertos.jolyglot.JolyglotGenerics;
import io.victoralbertos.mockery.api.built_in_interceptor.ErrorResponseAdapter;

/**
 * Prettify server error message.
 */
public final class ErrorAdapter implements ErrorResponseAdapter {
  private final JolyglotGenerics jolyglot;

  public ErrorAdapter() {
    this.jolyglot = new GsonSpeaker();
  }

  @Override public String adapt(String json) {
    try {
      return jolyglot
          .fromJson(json, ResponseError.class)
          .message;
    } catch (JsonSyntaxException e) {
      return json;
    }
  }

  private static class ResponseError {
    private final String message;

    public ResponseError(String message) {
      this.message = message;
    }
  }
}
