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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import javax.inject.Inject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Resolve networking dependencies providing a real implementation using Retrofit library.
 */
@Module
public class ApiModule {
  private final Retrofit retrofit;

  @Inject public ApiModule() {
    Gson gson = new GsonBuilder()
        //.registerTypeAdapterFactory(new AutoValueGsonTypeAdapterFactory())
        .create();

    this.retrofit = new Retrofit.Builder()
        .baseUrl("api_url")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
  }

}
