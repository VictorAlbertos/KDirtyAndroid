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

package app.presentation.foundation.presenter;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Store those screen names which require to be called onSyncScreen() due to an user interaction
 * or a received push notification when they come from a background state to a foreground one.
 */
@Singleton
public class SyncView {
  private final List<String> pendingScreens;

  @Inject SyncView() {
    this.pendingScreens = new ArrayList<>();
  }

  void addScreen(String screen) {
    if (!pendingScreens.contains(screen)) pendingScreens.add(screen);
  }

  boolean needToSync(Matcher matcher) {
    boolean needToSync = false;

    int index = 0;

    for (String screen : pendingScreens) {
      if (matcher.matchesTarget(screen)) {
        needToSync = true;
        break;
      }

      index++;
    }

    if (needToSync) pendingScreens.remove(index);
    return needToSync;
  }

  interface Matcher {
    boolean matchesTarget(String key);
  }
}
