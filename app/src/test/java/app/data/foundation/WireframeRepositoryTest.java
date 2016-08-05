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

package app.data.foundation;

import io.reactivecache.ReactiveCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class WireframeRepositoryTest {
  @Rule public TemporaryFolder testFolder = new TemporaryFolder();
  private WireframeRepository wireframeRepositoryUT;
  private static final String KEY = "mockModel";

  @Before public void init() {
    wireframeRepositoryUT = new WireframeRepository(
        new ReactiveCache.Builder().using(testFolder.getRoot(), new GsonSpeaker())
    );
  }

  @Test public void Verify_Put_And_Get_Success() {
    wireframeRepositoryUT.put(KEY, new MockModel()).subscribe();
    TestSubscriber<MockModel> subscriber = new TestSubscriber<>();
    wireframeRepositoryUT.<MockModel>get(KEY).subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValueCount(1);
    assertNotNull(subscriber.getOnNextEvents().get(0));
  }

  @Test public void Verify_Get_Failure() {
    TestSubscriber<MockModel> subscriber = new TestSubscriber<>();
    wireframeRepositoryUT.<MockModel>get(KEY).subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoValues();
    Throwable error = subscriber.getOnErrorEvents().get(0);
    assertEquals(error.getMessage(),
        "There is not cached data in wireframe repository for key mockModel");
  }

  @Test public void Verify_Put_Failure() {
    TestSubscriber<Void> subscriber = new TestSubscriber<>();
    wireframeRepositoryUT.put(KEY, null)
        .subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoValues();
    Throwable error = subscriber.getOnErrorEvents().get(0);
    assertEquals(error.getMessage(),
        "A null reference was supplied to be cached on the wireframe with key mockModel");
  }



}