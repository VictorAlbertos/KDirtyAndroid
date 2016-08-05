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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public final class SyncViewTest {
  private static final String KEY_1 = "key_1";
  private SyncView syncViewUT;

  @Before public void init() {
    syncViewUT = new SyncView();
  }

  @Test public void When_No_Added_Screen_Then_Need_To_Sync_Is_False() {
    boolean needToSync = syncViewUT.needToSync(new MatcherMock());
    assertFalse(needToSync);
  }

  @Test public void When_Added_Screen_Then_Need_To_Sync_Is_True() {
    syncViewUT.addScreen(KEY_1);
    boolean needToSync = syncViewUT.needToSync(new MatcherMock());
    assertThat(needToSync, is(true));
  }

  @Test public void After_Call_Need_To_Sync_Then_Need_To_Sync_Returns_False() {
    syncViewUT.addScreen(KEY_1);

    boolean needToSync = syncViewUT.needToSync(new MatcherMock());
    assertThat(needToSync, is(true));

    needToSync = syncViewUT.needToSync(new MatcherMock());
    assertThat(needToSync, is(false));
  }

  @Test public void After_Call_Need_To_Sync_Repeated_Times_Then_Need_To_Sync_Returns_False() {
    syncViewUT.addScreen(KEY_1);
    syncViewUT.addScreen(KEY_1);
    syncViewUT.addScreen(KEY_1);
    syncViewUT.addScreen(KEY_1);

    boolean needToSync = syncViewUT.needToSync(new MatcherMock());
    assertThat(needToSync, is(true));

    needToSync = syncViewUT.needToSync(new MatcherMock());
    assertThat(needToSync, is(false));
  }

  @Test public void Verify_With_Noise() {
    syncViewUT.addScreen("1");
    syncViewUT.addScreen(KEY_1);
    syncViewUT.addScreen("2");
    syncViewUT.addScreen("3");
    syncViewUT.addScreen(KEY_1);
    syncViewUT.addScreen("4");

    boolean needToSync = syncViewUT.needToSync(new MatcherMock());
    assertThat(needToSync, is(true));

    needToSync = syncViewUT.needToSync(new MatcherMock());
    assertThat(needToSync, is(false));
  }

  private static class MatcherMock implements SyncView.Matcher {
    @Override public boolean matchesTarget(String key) {
      return KEY_1.equals(key);
    }
  }


}