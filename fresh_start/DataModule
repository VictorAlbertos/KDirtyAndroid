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

package app.data.foundation.dagger;

import app.data.foundation.Resources;
import app.data.foundation.net.ApiModule;
import app.presentation.foundation.BaseApp;
import com.google.gson.TypeAdapterFactory;
import dagger.Module;
import dagger.Provides;
import io.reactivecache.ReactiveCache;
import io.victoralbertos.jolyglot.GsonAutoValueSpeaker;
import javax.inject.Singleton;

/**
 * Resolve the dependencies for data layer. For networking dependencies {@see ApiModule}, which its
 * concrete implementation depends on the current build variant in order to be able to provide a
 * mock implementation for the networking layer.
 */
@Module(includes = {ApiModule.class})
public final class DataModule {

  @Singleton @Provides Resources provideUiUtils(BaseApp baseApp) {
    return baseApp::getString;
  }

  @Singleton
  @Provides public ReactiveCache provideReactiveCache(BaseApp baseApp) {
    return new ReactiveCache.Builder()
        .using(baseApp.getFilesDir(), new GsonAutoValueSpeaker() {
          @Override protected TypeAdapterFactory autoValueGsonTypeAdapterFactory() {
            return null; //new AutoValueGsonTypeAdapterFactory();
          }
        });
  }
}
