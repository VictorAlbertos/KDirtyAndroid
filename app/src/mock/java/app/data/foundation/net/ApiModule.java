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

import app.data.sections.users.GithubUsersApi;
import dagger.Module;
import dagger.Provides;
import io.victoralbertos.mockery.api.Mockery;
import javax.inject.Singleton;

/**
 * Resolve networking dependencies providing a mock implementation using Mockery library.
 */
@Module
public class ApiModule {

 @Singleton @Provides public GithubUsersApi provideGithubUsersApi() {
    return new Mockery.Builder<GithubUsersApi>()
        .mock(GithubUsersApi.class)
        .build();
  }

}
